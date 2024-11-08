package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_BUY_CREDITS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.domain.Player;
import com.polifono.domain.Transaction;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.service.ITransactionService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.player.PlayerService;

import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PaymentController {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final ITransactionService transactionService;
    private final PlayerService playerService;

    @GetMapping("/buycredits")
    public String buyCredits() {
        return URL_BUY_CREDITS;
    }

    @PostMapping("/buycredits")
    public String buyCreditsSubmit(final Model model,
            @RequestParam("quantity") int quantity,
            Locale locale) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = currentUser.get().getUser();

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
        // This item is not a transaction yet, but it already contains the player, the quantity of credits and the date.
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
    private String openPagSeguro(Transaction t, Player player, int quantity) throws PagSeguroServiceException {
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
