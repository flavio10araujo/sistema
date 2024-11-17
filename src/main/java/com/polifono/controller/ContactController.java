package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.service.RecaptchaService;
import com.polifono.service.SendEmailService;
import com.polifono.service.contact.ContactModelPreparer;
import com.polifono.service.contact.ContactService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ContactController {

    private final RecaptchaService captchaService;
    private final SendEmailService sendEmailService;
    private final ContactService contactService;
    private final ContactModelPreparer contactModelPreparer;

    @PostMapping("/contact")
    public String contactSubmit(final Model model,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam("message") String message,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse,
            HttpServletRequest request,
            Locale locale) {

        String captchaVerifyMessage = captchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);
        if (captchaVerifyMessage != null && !captchaVerifyMessage.isEmpty()) {
            contactModelPreparer.prepareModelForCaptchaError(model, locale);
            return URL_INDEX;
        }

        String validationMessage = contactService.validateContact(email, message, locale);
        if (!validationMessage.isEmpty()) {
            contactModelPreparer.prepareModelForContactError(model, validationMessage);
            return URL_INDEX;
        }

        sendEmailService.sendEmailContact(email, message);
        contactModelPreparer.prepareModelForContactSuccess(model);
        return URL_INDEX;
    }
}
