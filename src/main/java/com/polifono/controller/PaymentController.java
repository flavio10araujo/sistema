package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_BUY_CREDITS;

import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.Min;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.model.CurrentUser;
import com.polifono.model.entity.Player;
import com.polifono.service.handler.PaymentHandler;
import com.polifono.service.impl.SecurityService;

import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PaymentController {

    private final MessageSource messagesResource;
    private final SecurityService securityService;
    private final PaymentHandler paymentHandler;

    @GetMapping("/buycredits")
    public String buyCredits() {
        return URL_BUY_CREDITS;
    }

    @Validated
    @PostMapping("/buycredits")
    public String buyCreditsSubmit(final Model model,
            @RequestParam("quantity") @Min(1) int quantity,
            Locale locale) {

        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();
        if (currentUser.isEmpty()) {
            return REDIRECT_HOME;
        }

        Player player = currentUser.get().getUser();

        String validationError = paymentHandler.validatePurchaseEligibility(player, quantity, locale);
        if (validationError != null) {
            model.addAttribute("codRegister", "2");
            model.addAttribute("msg", validationError);
            return URL_BUY_CREDITS;
        }

        try {
            String redirectUrl = paymentHandler.processPayment(player, quantity);
            return "redirect:" + redirectUrl;
        } catch (PagSeguroServiceException e) {
            model.addAttribute("codRegister", "2");
            model.addAttribute("msg", messagesResource.getMessage("msg.credits.error.access", null, locale));
            return URL_BUY_CREDITS;
        }
    }
}
