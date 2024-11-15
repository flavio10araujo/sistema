package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.polifono.model.entity.Player;
import com.polifono.service.impl.player.PlayerHandler;
import com.polifono.service.impl.player.PlayerPasswordResetHandler;
import com.polifono.service.impl.player.PlayerService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PasswordController {

    private final PlayerService playerService;
    private final PlayerHandler playerManagementService;
    private final PlayerPasswordResetHandler passwordResetHandler;

    @GetMapping("/passwordreset")
    public String passwordReset(final Model model) {
        passwordResetHandler.setupPasswordResetAttributes(model);
        return URL_INDEX;
    }

    @PostMapping("/passwordreset")
    public String passwordResetSubmit(final Model model,
            @ModelAttribute("player") @NonNull Player player,
            Locale locale) {

        Optional<Player> playerOptional = playerService.findByEmail(player.getEmail());
        boolean byLogin = playerOptional.isEmpty();

        if (playerOptional.isEmpty()) {
            playerOptional = playerService.findByLogin(player.getEmail());

            if (playerOptional.isEmpty()) {
                return passwordResetHandler.handlePlayerNotFound(model, player, locale);
            }
        }

        Player existingPlayer = playerOptional.get();

        if (!player.getPasswordReset().equals(existingPlayer.getPasswordReset())) {
            return passwordResetHandler.handleIncorrectResetCode(model, player, locale);
        }

        existingPlayer.setPassword(player.getPassword());
        String validationError = playerManagementService.validateChangePasswordPlayer(existingPlayer, locale);

        if (!validationError.isEmpty()) {
            return passwordResetHandler.handleValidationError(model, player, validationError);
        }

        passwordResetHandler.updatePlayerPassword(existingPlayer);
        passwordResetHandler.setupPasswordResetSuccessAttributes(model, byLogin, existingPlayer, locale);
        return URL_INDEX;
    }

    @PostMapping("/passwordresetresend")
    public String passwordResetResend(final Model model,
            @ModelAttribute("playerResend") @NonNull Player playerResend,
            Locale locale) {

        Optional<Player> playerOptional = playerService.findByEmail(playerResend.getEmail());
        boolean byLogin = playerOptional.isEmpty();

        if (playerOptional.isEmpty()) {
            playerOptional = playerService.findByLogin(playerResend.getEmail());

            if (playerOptional.isEmpty()) {
                return passwordResetHandler.handlePlayerNotFound(model, playerResend, locale);
            }
        }

        passwordResetHandler.resetPlayerPassword(playerOptional.get());
        return passwordResetHandler.handlePasswordResetCodeSent(model, playerOptional.get(), byLogin, locale);
    }
}
