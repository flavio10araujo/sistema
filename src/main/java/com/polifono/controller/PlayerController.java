package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.polifono.model.entity.Player;
import com.polifono.service.SendEmailService;
import com.polifono.service.player.PlayerHandler;
import com.polifono.service.player.PlayerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerHandler playerHandler;
    private final SendEmailService emailSendUtil;

    @Validated
    @PostMapping("/players")
    public synchronized String createPlayer(HttpServletRequest request,
            final Model model,
            @ModelAttribute("player") @NonNull Player player,
            Locale locale) {

        String emailError = playerHandler.validateAndSanitizeEmail(player, locale);
        if (emailError != null) {
            prepareModelForPlayerCreation(model, player, 2, emailError);
            return URL_INDEX;
        }

        String validationError = playerHandler.validateCreatePlayer(player, locale);
        if (validationError != null) {
            prepareModelForPlayerCreation(model, player, 2, validationError);
            return URL_INDEX;
        }

        playerHandler.formatPlayerNames(player);
        playerService.create(player);
        prepareModelForPlayerCreation(model, player, 1, null);
        emailSendUtil.sendEmailConfirmRegister(player);
        loginPlayer(request, player.getEmail(), player.getPassword());

        return REDIRECT_HOME;
    }

    private void prepareModelForPlayerCreation(Model model, Player player, int codRegister, String msg) {
        model.addAttribute("playerResend", new Player());
        model.addAttribute("player", player);
        model.addAttribute("codRegister", codRegister);
        if (msg != null) {
            model.addAttribute("msgRegister", "<br />" + msg);
        }
    }

    private void loginPlayer(HttpServletRequest request, String email, String password) {
        try {
            request.login(email, password);
        } catch (ServletException e) {
            log.error("Error in the login of the player {} ", email, e);
        }
    }
}
