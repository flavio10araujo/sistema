package com.polifono.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Player;
import com.polifono.domain.bean.CurrentUser;
import com.polifono.service.impl.RecaptchaService;
import com.polifono.util.EmailSendUtil;
import com.polifono.util.EmailUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    public static final String URL_INDEX = "index";
    public static final String URL_CONTACT = "contact";
    public static final String URL_CONTACTOPEN = "index";
    public static final String REDIRECT_GAMES = "redirect:/games";

    @Autowired RecaptchaService captchaService;

    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public final String index(final Model model) {

        // If the player is not logged.
        if (this.currentAuthenticatedUser() == null) {
            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_INDEX;
        } else {
            return REDIRECT_GAMES;
        }
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public final String index(final Model model, @RequestParam Optional<String> error) {
        LOGGER.debug("Getting login page, error={}", error);
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        model.addAttribute("error", error);
        return URL_INDEX;
    }

    @RequestMapping(value = { "/contact" }, method = RequestMethod.GET)
    public final String contact() {
        // If the user is logged in.
        if (this.currentAuthenticatedUser() != null) {
            return URL_CONTACT;
        } else {
            return URL_CONTACTOPEN;
        }
    }

    @RequestMapping(value = { "/contactOld" }, method = RequestMethod.POST)
    public final String contactsubmitOld(final Model model, @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam("message") String message,
            @RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request) {

        boolean logged = false;

        CurrentUser currentUser = currentAuthenticatedUser();

        // If the user is logged in, get his email.
        if (currentUser != null) {
            logged = true;
            email = currentUser.getUser().getEmail();
        }

        String captchaVerifyMessage = captchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);

        if (captchaVerifyMessage != null && !captchaVerifyMessage.isEmpty()) {
            //Map<String, Object> response = new HashMap<>();
            //response.put("message", captchaVerifyMessage);
            //return ResponseEntity.badRequest().body(response);

            // If there are errors.
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", "Por favor, marque o campo Não sou um robô.");

            if (logged) {
                return URL_CONTACT;
            } else {
                model.addAttribute("player", new Player());
                model.addAttribute("playerResend", new Player());
                return URL_CONTACTOPEN;
            }
        }

        String msg = this.validateContact(email, message);

        // If there are errors.
        if (!msg.equals("")) {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", msg);

            if (logged) {
                return URL_CONTACT;
            } else {
                model.addAttribute("player", new Player());
                model.addAttribute("playerResend", new Player());
                return URL_CONTACTOPEN;
            }
        }

        EmailSendUtil.sendEmailContact(email, message);

        model.addAttribute("message", "success");

        if (logged) {
            return URL_CONTACT;
        } else {
            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_CONTACTOPEN;
        }
    }

    @RequestMapping(value = { "/contact" }, method = RequestMethod.POST)
    public final String contactsubmit(final Model model, @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam("message") String message, HttpServletRequest request) {

        boolean logged = false;

        CurrentUser currentUser = currentAuthenticatedUser();

        // If the user is logged in, get his email.
        if (currentUser != null) {
            logged = true;
            email = currentUser.getUser().getEmail();
        }

        String msg = this.validateContact(email, message);

        // If there are errors.
        if (!msg.equals("")) {
            model.addAttribute("message", "error");
            model.addAttribute("messageContent", msg);

            if (logged) {
                return URL_CONTACT;
            } else {
                model.addAttribute("player", new Player());
                model.addAttribute("playerResend", new Player());
                return URL_CONTACTOPEN;
            }
        }

        EmailSendUtil.sendEmailContact(email, message);

        model.addAttribute("message", "success");

        if (logged) {
            return URL_CONTACT;
        } else {
            model.addAttribute("player", new Player());
            model.addAttribute("playerResend", new Player());
            return URL_CONTACTOPEN;
        }
    }

    public String validateContact(String email, String message) {
        String msg = "";

        if (email == null || email.equals("")) {
            msg = msg + "O e-mail precisa ser informado.";
        } else if (!EmailUtil.validateEmail(email)) {
            msg = msg + "O e-mail informado não é válido.";
        }

        if (message == null || message.equals("")) {
            msg = ("".equals(msg)) ? "A mensagem precisa ser informada." : msg + "<br />A mensagem precisa ser informada.";
        }

        return msg;
    }
}
