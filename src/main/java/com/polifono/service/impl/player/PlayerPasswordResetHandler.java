package com.polifono.service.impl.player;

import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.polifono.common.util.PasswordUtil;
import com.polifono.common.util.RandomStringGenerator;
import com.polifono.model.entity.Player;
import com.polifono.service.impl.SendEmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerPasswordResetHandler {

    private final MessageSource messagesResource;
    private final PlayerService playerService;
    private final SendEmailService emailSendUtil;

    public void setupPasswordResetAttributes(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
    }

    public void setupPasswordResetSuccessAttributes(Model model, boolean byLogin, Player playerOldPlayer, Locale locale) {
        String loginInfo = byLogin ? playerOldPlayer.getLogin() : playerOldPlayer.getEmail();
        addAttributesToModel(model, 1, new Player(), messagesResource.getMessage("msg.passwordReset.success", new Object[] { loginInfo }, locale));
    }

    public String handlePlayerNotFound(Model model, Player player, Locale locale) {
        addAttributesToModel(model, 2, player, messagesResource.getMessage("msg.passwordReset.loginNotFound", new Object[] { player.getEmail() }, locale));
        return URL_INDEX;
    }

    public String handleIncorrectResetCode(Model model, Player player, Locale locale) {
        addAttributesToModel(model, 2, player, messagesResource.getMessage("msg.passwordReset.codeIncorrect", null, locale));
        return URL_INDEX;
    }

    public String handleValidationError(Model model, Player player, String msg) {
        addAttributesToModel(model, 2, player, msg);
        return URL_INDEX;
    }

    public String handlePasswordResetCodeSent(Model model, Player player, boolean byLogin, Locale locale) {
        if (byLogin) {
            Player teacher = player.getCreator();
            teacher.setName(player.getName());
            teacher.setPasswordReset(player.getPasswordReset());
            return handlePasswordResetCodeSent(model, teacher, locale);
        }

        return handlePasswordResetCodeSent(model, player, locale);
    }

    public void updatePlayerPassword(Player playerOldPlayer) {
        playerOldPlayer.setPassword(PasswordUtil.encryptPassword(playerOldPlayer.getPassword()));
        playerOldPlayer.setPasswordReset("");
        playerService.save(playerOldPlayer);
    }

    public void resetPlayerPassword(Player player) {
        player.setPasswordReset(RandomStringGenerator.generate(10));
        playerService.save(player);
    }

    private void addAttributesToModel(Model model, int codRegister, Player player, String message) {
        model.addAttribute("codRegister", codRegister);
        model.addAttribute("player", player);
        model.addAttribute("playerResend", new Player());
        model.addAttribute("msgRegister", "<br />" + message);
    }

    private String handlePasswordResetCodeSent(Model model, Player player, Locale locale) {
        addAttributesToModel(model, 1, new Player(),
                messagesResource.getMessage("msg.passwordReset.codeSent", new Object[] { player.getEmail() }, locale));
        emailSendUtil.sendEmailPasswordReset(player);
        return URL_INDEX;
    }
}
