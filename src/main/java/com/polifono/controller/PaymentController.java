package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_BUY_CREDITS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.domain.Player;
import com.polifono.domain.Transaction;
import com.polifono.service.IPlayerService;
import com.polifono.service.ITransactionService;
import com.polifono.service.impl.SendEmailService;

import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.TransactionSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PaymentController extends BaseController {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final MessageSource messagesResource;
    private final ITransactionService transactionService;
    private final IPlayerService playerService;
    private final SendEmailService emailSendUtil;

    @RequestMapping(value = { "/buycredits" }, method = RequestMethod.GET)
    public final String buyCredits(final Model model) {
        return URL_BUY_CREDITS;
    }

    @RequestMapping(value = { "/buycredits" }, method = RequestMethod.POST)
    public final String buyCreditsSubmit(final Model model, @RequestParam("quantity") Integer quantity, Locale locale) {

        Player player = Objects.requireNonNull(currentAuthenticatedUser()).getUser();

        // If the player has not confirmed his e-mail yet.
        // And the player has not informed his/her facebook.
        if (!playerService.isEmailConfirmed(player)
                && player.getIdFacebook() == null) {

            model.addAttribute("codRegister", "2");
            model.addAttribute("msg",
                    "Para poder adquirir créditos é necessário primeiramente confirmar o seu e-mail.<br />No momento do seu cadastro, nós lhe enviamos um e-mail com um código de ativação.<br />Acesse seu email e verifique o código enviado.<br />Caso não tenha mais o código, clique <a href='/emailconfirmation'>aqui</a>.");
            return URL_BUY_CREDITS;
        }

        // TEACHER and ADMIN don't have limitations to buy credits.
        if (player.getRole().equals("USER")) {
            int creditsBuyMin = configsCreditsProperties.getMinBuyCredits();
            int creditsBuyMax = configsCreditsProperties.getMaxBuyCredits();

            if (quantity < creditsBuyMin || quantity > creditsBuyMax) {
                model.addAttribute("codRegister", "2");
                model.addAttribute("msg", "A quantidade de créditos deve ser entre " + creditsBuyMin + " e " + creditsBuyMax + ".");
                return URL_BUY_CREDITS;
            }
        }

        // Register an item in T012_TRANSACTION.
        // This item is not a transaction yet, but it already contain the player, the quantity of credits and the date.
        // The T012.C002_ID will be passed in the attribute "reference".
        Transaction t = new Transaction();
        t.setPlayer(player);
        t.setQuantity(quantity);
        t.setDtInc(new Date());
        t.setClosed(false);
        transactionService.save(t);

        try {
            return "redirect:" + openPagSeguro(t, player, quantity);
        } catch (PagSeguroServiceException e) {
            log.debug("Error buying credits", e);

            model.addAttribute("codRegister", "2");
            model.addAttribute("msg", messagesResource.getMessage("msg.credits.error.access", null, locale));
            return URL_BUY_CREDITS;
        }
    }

    /**
     * This method connects to the payment system and create a code.
     * This code is the checkout.
     * The payment system return and URL. Ex.: https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code=EAE29CA5383892D224E9AF9FEFF0FC43
     * This code is not the transaction code yet.
     */
    public String openPagSeguro(Transaction t, Player player, int quantity) throws PagSeguroServiceException {
        Checkout checkout = new Checkout();

        checkout.addItem(
                "0001", // Item's number.
                PagSeguroSystem.getPagSeguroPaymentServiceNfDescription(), // Item's name.
                quantity, // Item's quantity.
                this.getPriceForEachUnity(quantity), // Price for each unity.
                0L, // Weight.
                null // ShippingCost
        );

        checkout.setShippingCost(new BigDecimal("0.00"));

        checkout.setSender(
                player.getFullName(), // Client's name.
                player.getEmail()
        );

        checkout.setCurrency(Currency.BRL);

        // Sets a reference code for this payment request. The T012.C002_ID is used in this attribute.
        checkout.setReference("" + t.getId());

        Boolean onlyCheckoutCode = false;
        String checkoutURL = checkout.register(PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);

        log.debug(checkoutURL);

        return checkoutURL;
    }

    @RequestMapping(value = { "/pagseguroreturn" }, method = RequestMethod.GET)
    public final String returnPagSeguro(final Model model, @RequestParam("tid") String transactionCode, Locale locale) {

        log.debug("/pagseguroreturn tid=", transactionCode);
        System.out.println("/pagseguroreturn tid=" + transactionCode);

        if (transactionCode == null || transactionCode.isEmpty())
            return REDIRECT_HOME;

        List<Transaction> transactions = transactionService.findByCode(transactionCode);

        model.addAttribute("codRegister", "1");
        model.addAttribute("msg", messagesResource.getMessage("msg.credits.thanks", null, locale));

        // If the transaction is already registered.
        if (transactions != null && !transactions.isEmpty())
            return URL_BUY_CREDITS;

        br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = null;

        try {
            pagSeguroTransaction = TransactionSearchService.searchByCode(PagSeguroConfig.getAccountCredentials(), transactionCode);
        } catch (PagSeguroServiceException e) {
            log.debug("/pagseguroreturn ERROR with tid= {} ", transactionCode);

            // If the transactionCode is not valid.
            if (pagSeguroTransaction == null) {
                log.debug("/pagseguroreturn TID not valid tid {}", transactionCode);
                return REDIRECT_HOME;
            }
        }

        Optional<Transaction> transactionOpt = transactionService.findById(Integer.parseInt(pagSeguroTransaction.getReference()));

        if (transactionOpt.isEmpty()) {
            return REDIRECT_HOME;
        }

        Transaction transaction = transactionOpt.get();

        transaction.setCode(pagSeguroTransaction.getCode());
        transaction.setReference(pagSeguroTransaction.getReference());
        transaction.setDate(pagSeguroTransaction.getDate());
        transaction.setLastEventDate(pagSeguroTransaction.getLastEventDate());
        transaction.setType(pagSeguroTransaction.getType().getValue());
        transaction.setStatus(pagSeguroTransaction.getStatus().getValue());
        transaction.setPaymentMethodType(pagSeguroTransaction.getPaymentMethod().getType().getValue());
        transaction.setPaymentMethodCode(pagSeguroTransaction.getPaymentMethod().getCode().getValue());
        transaction.setGrossAmount(pagSeguroTransaction.getGrossAmount());
        transaction.setDiscountAmount(pagSeguroTransaction.getDiscountAmount());
        transaction.setFeeAmount(pagSeguroTransaction.getFeeAmount());
        transaction.setNetAmount(pagSeguroTransaction.getNetAmount());
        transaction.setExtraAmount(pagSeguroTransaction.getExtraAmount());
        transaction.setInstallmentCount(pagSeguroTransaction.getInstallmentCount());
        transaction.setItemCount(pagSeguroTransaction.getItemCount());
        transaction.setEscrowEndDate(pagSeguroTransaction.getEscrowEndDate());
        transaction.setCancellationSource(pagSeguroTransaction.getCancellationSource());
        transaction.setPaymentLink(pagSeguroTransaction.getPaymentLink());

        transactionService.save(transaction);

        // If the status is:
        // PAID (3) OR AVAILABLE (4)
        if (pagSeguroTransaction.getStatus().getValue() == 3 || pagSeguroTransaction.getStatus().getValue() == 4) {
            // Add credits to the player.
            playerService.addCreditsToPlayer(transaction.getPlayer().getId(), transaction.getQuantity());

            // Register this transaction as finished (to avoid that the player receive twice or more the credits).
            transaction.setClosed(true);
            transactionService.save(transaction);

            // Send e-mail.
            emailSendUtil.sendEmailPaymentRegistered(transaction.getPlayer(), transaction.getQuantity());
        }

        return URL_BUY_CREDITS;
    }

    @RequestMapping(value = "/pagseguronotification", method = RequestMethod.POST)
    public @ResponseBody String pagseguronotification(final Model model, @RequestParam("notificationCode") String notificationCode) {

        log.debug("/pagseguronotification notificationCode = {}", notificationCode);

        if (notificationCode == null || notificationCode.isEmpty())
            return null;

        br.com.uol.pagseguro.domain.Transaction pagSeguroTransaction = null;

        try {
            pagSeguroTransaction = NotificationService.checkTransaction(PagSeguroConfig.getAccountCredentials(), notificationCode);
        } catch (PagSeguroServiceException e) {
            log.debug("/pagseguronotification ERROR with notificationCode = {}", notificationCode);
            log.debug("error", e);

            // If the notificationCode is not valid.
            if (pagSeguroTransaction == null) {
                log.debug("/pagseguronotification TID not valid notificationCode = {}", notificationCode);
                return null;
            }
        }

        // TODO - preciso entender melhor como funcionam os status da transação.
        // TODO - posso considerar o status PAID ou CANCELLED como sendo o último?
        // pagSeguroTransaction.getReference() == T012.C012_ID
        Optional<Transaction> transactionOpt = transactionService.findById(Integer.parseInt(pagSeguroTransaction.getReference()));

        if (transactionOpt.isEmpty()) {
            return null;
        }

        Transaction transaction = transactionOpt.get();

        // If the player has already received this credits.
        if (transaction.isClosed())
            return null;

        Transaction tNew = new Transaction();
        tNew.setPlayer(transaction.getPlayer());
        tNew.setQuantity(transaction.getQuantity());
        tNew.setDtInc(transaction.getDtInc());
        tNew.setClosed(false);
        tNew.setCode(pagSeguroTransaction.getCode());
        tNew.setReference(pagSeguroTransaction.getReference());
        tNew.setDate(pagSeguroTransaction.getDate());
        tNew.setLastEventDate(pagSeguroTransaction.getLastEventDate());
        tNew.setType(pagSeguroTransaction.getType().getValue());
        tNew.setStatus(pagSeguroTransaction.getStatus().getValue());
        tNew.setPaymentMethodType(pagSeguroTransaction.getPaymentMethod().getType().getValue());
        tNew.setPaymentMethodCode(pagSeguroTransaction.getPaymentMethod().getCode().getValue());
        tNew.setGrossAmount(pagSeguroTransaction.getGrossAmount());
        tNew.setDiscountAmount(pagSeguroTransaction.getDiscountAmount());
        tNew.setFeeAmount(pagSeguroTransaction.getFeeAmount());
        tNew.setNetAmount(pagSeguroTransaction.getNetAmount());
        tNew.setExtraAmount(pagSeguroTransaction.getExtraAmount());
        tNew.setInstallmentCount(pagSeguroTransaction.getInstallmentCount());
        tNew.setItemCount(pagSeguroTransaction.getItemCount());
        tNew.setEscrowEndDate(pagSeguroTransaction.getEscrowEndDate());
        tNew.setCancellationSource(pagSeguroTransaction.getCancellationSource());
        tNew.setPaymentLink(pagSeguroTransaction.getPaymentLink());

        transactionService.save(tNew);

        // If the status is:
        // PAID (3) OR AVAILABLE (4)
        if (pagSeguroTransaction.getStatus().getValue() == 3 || pagSeguroTransaction.getStatus().getValue() == 4) {
            // Add credits to the player.
            playerService.addCreditsToPlayer(transaction.getPlayer().getId(), transaction.getQuantity());

            // Register this transaction as finished (to avoid that the player receive twice or more the credits).
            transaction.setClosed(true);
            transactionService.save(transaction);

            // Send e-mail.
            emailSendUtil.sendEmailPaymentRegistered(transaction.getPlayer(), transaction.getQuantity());
        }

        return URL_BUY_CREDITS;
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
