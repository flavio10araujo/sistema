package com.polifono.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.service.helper.ContactHelperService;
import com.polifono.service.impl.ContactService;
import com.polifono.service.impl.RecaptchaService;
import com.polifono.service.impl.SendEmailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ContactController {

    private final RecaptchaService captchaService;
    private final SendEmailService sendEmailService;
    private final ContactService contactService;
    private final ContactHelperService contactHelperService;

    @PostMapping("/contact")
    public String contactSubmit(final Model model,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam("message") String message,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse,
            HttpServletRequest request,
            Locale locale) {

        String captchaVerifyMessage = captchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);
        if (captchaVerifyMessage != null && !captchaVerifyMessage.isEmpty()) {
            return contactHelperService.handleCaptchaError(model, locale);
        }

        String validationMessage = contactService.validateContact(email, message, locale);
        if (!validationMessage.isEmpty()) {
            return contactHelperService.handleError(model, validationMessage);
        }

        sendEmailService.sendEmailContact(email, message);
        return contactHelperService.handleSuccess(model);
    }
}
