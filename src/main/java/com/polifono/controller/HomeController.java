package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_GAMES;
import static com.polifono.common.TemplateConstants.URL_INDEX;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Player;
import com.polifono.service.impl.RecaptchaService;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.util.EmailUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final SecurityService securityService;
    private final RecaptchaService captchaService;
    private final SendEmailService sendEmailService;

    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public final String index(final Model model) {
        if (securityService.isAuthenticated()) {
            return REDIRECT_GAMES;
        } else {
            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_INDEX;
        }
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public final String index(final Model model, @RequestParam Optional<String> error) {
        log.debug("Getting login page, error={}", error);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        model.addAttribute("error", error);
        return URL_INDEX;
    }

    @RequestMapping(value = { "/contact" }, method = RequestMethod.POST)
    public final String contactSubmit(final Model model, @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam("message") String message,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request) {

        String captchaVerifyMessage = captchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);

        if (captchaVerifyMessage != null && !captchaVerifyMessage.isEmpty()) {
            // If there are errors.
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", "Por favor, marque o campo Não sou um robô.");

            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_INDEX;
        }

        String msg = this.validateContact(email, message);

        // If there are errors.
        if (!msg.isEmpty()) {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", msg);

            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_INDEX;
        }

        sendEmailService.sendEmailContact(email, message);

        model.addAttribute("message", "success");

        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        return URL_INDEX;
    }

    public String validateContact(String email, String message) {
        String msg = "";

        if (email == null || email.isEmpty()) {
            msg = msg + "O e-mail precisa ser informado.";
        } else if (!EmailUtil.validateEmail(email)) {
            msg = msg + "O e-mail informado não é válido.";
        }

        if (message == null || message.isEmpty()) {
            msg = (msg.isEmpty()) ? "A mensagem precisa ser informada." : msg + "<br />A mensagem precisa ser informada.";
        }

        return msg;
    }
}
