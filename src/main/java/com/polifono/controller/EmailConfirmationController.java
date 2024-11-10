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
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EmailConfirmationController {

    private final PlayerService playerService;
    private final SendEmailService emailSendUtil;
    private final MessageSource messagesResource;
    private final GenerateRandomStringService generateRandomStringService;

    @GetMapping("/emailconfirmation")
    public String emailConfirmation(final Model model) {
        model.addAttribute("playerResend", new Player());
        model.addAttribute("player", new Player());
        return URL_EMAIL_CONFIRMATION;
    }

    @Validated
    @PostMapping("/emailconfirmation")
    public String emailConfirmationSubmit(final Model model,
            @ModelAttribute("player") @NonNull Player player,
            Locale locale) {

        Optional<Player> playerOldOpt = playerService.findByEmail(player.getEmail());

        if (playerOldOpt.isEmpty()) {
            model.addAttribute("playerResend", new Player());
            model.addAttribute("player", player);
            model.addAttribute("codRegister", 2);
            model.addAttribute("msgRegister",
                    "<br />" + messagesResource.getMessage("msg.emailConfirmation.notFound", new Object[] { player.getEmail() }, locale));
            return URL_EMAIL_CONFIRMATION;
        }

        // If the player was already confirmed.
        if (playerOldOpt.get().isIndEmailConfirmed()) {
            model.addAttribute("playerResend", new Player());
            model.addAttribute("player", player);
            model.addAttribute("codRegister", 2);
            model.addAttribute("msgRegister", "<br />" + messagesResource.getMessage("msg.emailConfirmation.alreadyConfirmed", null, locale));
            return URL_EMAIL_CONFIRMATION;
        }

        // If the code informed is not correct.
        if (!player.getEmailConfirmed().equals(playerOldOpt.get().getEmailConfirmed())) {
            model.addAttribute("playerResend", new Player());
            model.addAttribute("player", player);
            model.addAttribute("codRegister", 2);
            model.addAttribute("msgRegister", "<br />" + messagesResource.getMessage("msg.emailConfirmation.incorrectCode", null, locale));
            return URL_EMAIL_CONFIRMATION;
        }

        playerOldOpt.get().setIndEmailConfirmed(true);
        playerService.save(playerOldOpt.get());

        model.addAttribute("playerResend", new Player());
        model.addAttribute("player", new Player());
        model.addAttribute("codRegister", 1);
        model.addAttribute("msgRegister",
                "<br />" + messagesResource.getMessage("msg.emailConfirmation.success", new Object[] { player.getEmail() }, locale));
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
            model.addAttribute("playerResend", player);
            model.addAttribute("player", new Player());
            model.addAttribute("codRegister", 2);
            model.addAttribute("msgRegister",
                    "<br />" + messagesResource.getMessage("msg.emailConfirmation.notFound", new Object[] { player.getEmail() }, locale));
            return URL_EMAIL_CONFIRMATION;
        }

        // If the player was already confirmed.
        if (playerOldOpt.get().isIndEmailConfirmed()) {
            model.addAttribute("playerResend", player);
            model.addAttribute("player", new Player());
            model.addAttribute("codRegister", 2);
            model.addAttribute("msgRegister", "<br />" + messagesResource.getMessage("msg.emailConfirmation.alreadyConfirmed", null, locale));
            return URL_EMAIL_CONFIRMATION;
        }

        playerOldOpt.get().setEmailConfirmed(generateRandomStringService.generate(10));
        playerService.save(playerOldOpt.get());
        emailSendUtil.sendEmailConfirmRegister(playerOldOpt.get());

        model.addAttribute("playerResend", new Player());
        model.addAttribute("player", new Player());
        model.addAttribute("codRegister", 1);
        model.addAttribute("msgRegister",
                "<br />" + messagesResource.getMessage("msg.emailConfirmation.resend", new Object[] { player.getEmail() }, locale));

        return URL_EMAIL_CONFIRMATION;
    }
}
