package com.polifono.service.transaction;

import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguroV2.enums.PaymentMethodType;
import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.Transaction;
import com.polifono.model.enums.Role;
import com.polifono.service.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class PaymentHandler {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final MessageSource messagesResource;
    private final TransactionService transactionService;
    private final PlayerService playerService;

    public String validatePurchaseEligibility(Player player, int quantity, Locale locale) {
        if (!playerService.isEmailConfirmed(player) && player.getIdFacebook() == null) {
            return messagesResource.getMessage("msg.credits.NeedConfirmEmail", null, locale);
        }

        if (Role.USER.equals(player.getRole())) {
            int creditsBuyMin = configsCreditsProperties.getMinBuyCredits();
            int creditsBuyMax = configsCreditsProperties.getMaxBuyCredits();
            if (quantity < creditsBuyMin || quantity > creditsBuyMax) {
                return messagesResource.getMessage("msg.credits.quantity.rule", new Object[] { creditsBuyMin, creditsBuyMax }, locale);
            }
        }

        return null;
    }

    public String processPayment(String apiVersion, Player player, int quantity)
            throws PagSeguroServiceException, br.com.uol.pagseguroV2.exception.PagSeguroServiceException, IOException {
        Transaction transaction = createTransaction(player, quantity);
        return ("V2".equals(apiVersion) ? openPagSeguroV2(transaction, player, quantity) : openPagSeguro(transaction, player, quantity));
    }

    /**
     * Register an item in T012_TRANSACTION.
     * This item is not a transaction yet, but it already contains the player, the quantity of credits and the date.
     * The T012.C002_ID will be passed in the attribute "reference".
     */
    private Transaction createTransaction(Player player, int quantity) {
        Transaction transaction = new Transaction();
        transaction.setPlayer(player);
        transaction.setQuantity(quantity);
        transaction.setDtInc(new Date());
        transaction.setClosed(false);
        transactionService.save(transaction);
        return transaction;
    }

    /**
     * This method connects to the payment system and create a code.
     * This code is the checkout.
     * The payment system return a URL. Ex.: https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code=EAE29CA5383892D224E9AF9FEFF0FC43
     * This code is not the transaction code yet.
     */
    private String openPagSeguro(Transaction transaction, Player player, int quantity) throws PagSeguroServiceException {
        Checkout checkout = new Checkout();

        checkout.addItem(
                "0001", // Item's number.
                PagSeguroSystem.getPagSeguroPaymentServiceNfDescription(), // Item's name.
                quantity, // Item's quantity.
                getPriceForEachUnity(quantity), // Price for each unity.
                0L, // Weight.
                null // ShippingCost
        );

        checkout.setShippingCost(new BigDecimal("0.00"));
        checkout.setSender(player.getFullName(), player.getEmail());
        checkout.setCurrency(Currency.BRL);
        checkout.setReference("" + transaction.getId()); // Sets a reference code for this payment request. The T012.C002_ID is used in this attribute.

        Boolean onlyCheckoutCode = false;
        return checkout.register(PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);
    }

    private String openPagSeguroV2(Transaction transaction, Player player, int quantity)
            throws br.com.uol.pagseguroV2.exception.PagSeguroServiceException, IOException {
        br.com.uol.pagseguroV2.domain.checkout.Checkout checkout = new br.com.uol.pagseguroV2.domain.checkout.Checkout();

        checkout.setReferenceId("" + transaction.getId()); // Sets a reference code for this payment request. The T012.C002_ID is used in this attribute.
        checkout.setCustomerModifiable(true);

        checkout.addItem(
                "0001", // Item's number.
                br.com.uol.pagseguroV2.properties.PagSeguroSystem.getPagSeguroPaymentServiceNfDescription(), // Item's name.
                br.com.uol.pagseguroV2.properties.PagSeguroSystem.getPagSeguroPaymentServiceNfDescription(), // Item's description.
                quantity, // Item's quantity.
                getPriceForEachUnity(quantity * 100) // Price for each unity in cents.
        );

        checkout.setPaymentMethods(List.of(PaymentMethodType.CREDIT_CARD, PaymentMethodType.BOLETO, PaymentMethodType.PIX));
        checkout.setRedirectURL("https://www.polifono.com/pagseguroreturn");
        checkout.setPaymentNotificationUrls(List.of("https://www.polifono.com/pagseguronotification"));

        return checkout.register(br.com.uol.pagseguroV2.properties.PagSeguroConfig.getAccountCredentials());
    }

    private BigDecimal getPriceForEachUnity(int quantity) {
        BigDecimal price;

        if (quantity <= 25) {
            price = BigDecimal.valueOf(configsCreditsProperties.getPriceForEachUnityRange01());
        } else if (quantity <= 49) {
            price = BigDecimal.valueOf(configsCreditsProperties.getPriceForEachUnityRange02());
        } else {
            price = BigDecimal.valueOf(configsCreditsProperties.getPriceForEachUnityRange03());
        }

        return price.setScale(2, RoundingMode.HALF_UP);
    }
}
