package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.URL_EMAIL_CONFIRMATION;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.polifono.model.entity.Player;
import com.polifono.service.player.PlayerService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EmailConfirmationController {

    private static final int CODE_REGISTER_SUCCESS = 1;
    private static final int CODE_REGISTER_ERROR = 2;

    private final PlayerService playerService;
    private final MessageSource messagesResource;

    @GetMapping("/emailconfirmation")
    public String emailConfirmation(final Model model) {
        prepareModelForEmailConfirmation(model, new Player(), new Player(), 0, null);
        return URL_EMAIL_CONFIRMATION;
    }

    @Validated
    @PostMapping("/emailconfirmation")
    public String emailConfirmationSubmit(final Model model,
            @ModelAttribute("player") @NonNull Player player,
            Locale locale) {

        Optional<Player> playerOldOpt = playerService.findByEmail(player.getEmail());
        if (playerOldOpt.isEmpty()) {
            return prepareErrorModel(model, new Player(), player, "msg.emailConfirmation.notFound", locale);
        }

        Player playerOld = playerOldOpt.get();

        // If the player was already confirmed.
        if (playerOld.isIndEmailConfirmed()) {
            return prepareErrorModel(model, new Player(), player, "msg.emailConfirmation.alreadyConfirmed", locale);
        }

        // If the code informed is not correct.
        if (!player.getEmailConfirmed().equals(playerOld.getEmailConfirmed())) {
            return prepareErrorModel(model, new Player(), player, "msg.emailConfirmation.incorrectCode", locale);
        }

        playerService.confirmEmail(playerOld);

        prepareModelForEmailConfirmation(model, new Player(), new Player(), CODE_REGISTER_SUCCESS,
                messagesResource.getMessage("msg.emailConfirmation.success", new Object[] { player.getEmail() }, locale));
        return URL_EMAIL_CONFIRMATION;
    }

    @Validated
    @PostMapping("/emailconfirmationresend")
    public String emailConfirmationResend(final Model model,
            @ModelAttribute("playerResend") @NonNull Player player,
            Locale locale) {

        Optional<Player> playerOldOpt = playerService.findByEmail(player.getEmail());

        // If there is no player with this email.
        if (playerOldOpt.isEmpty()) {
            return prepareErrorModel(model, player, new Player(), "msg.emailConfirmation.notFound", locale);
        }

        Player playerOld = playerOldOpt.get();

        // If the player was already confirmed.
        if (playerOld.isIndEmailConfirmed()) {
            return prepareErrorModel(model, player, new Player(), "msg.emailConfirmation.alreadyConfirmed", locale);
        }

        playerService.resendEmailConfirmation(playerOld);

        prepareModelForEmailConfirmation(model, new Player(), new Player(), CODE_REGISTER_SUCCESS,
                messagesResource.getMessage("msg.emailConfirmation.resend", new Object[] { player.getEmail() }, locale));
        return URL_EMAIL_CONFIRMATION;
    }

    private void prepareModelForEmailConfirmation(Model model, Player playerResend, Player player, int codRegister, String msgRegister) {
        model.addAttribute("playerResend", playerResend);
        model.addAttribute("player", player);

        if (codRegister > 0) {
            model.addAttribute("codRegister", codRegister);
        }

        if (msgRegister != null) {
            model.addAttribute("msgRegister", "<br />" + msgRegister);
        }
    }

    private String prepareErrorModel(Model model, Player playerResend, Player player, String messageKey, Locale locale) {
        prepareModelForEmailConfirmation(model, playerResend, player, CODE_REGISTER_ERROR,
                messagesResource.getMessage(messageKey, new Object[] { player.getEmail() }, locale));
        return URL_EMAIL_CONFIRMATION;
    }
}
