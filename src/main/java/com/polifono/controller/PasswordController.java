package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_INDEX;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.polifono.domain.Player;
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerManagementService;
import com.polifono.service.impl.player.PlayerService;
import com.polifono.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PasswordController {

    private final PlayerService playerService;
    private final PlayerManagementService playerManagementService;
    private final SendEmailService emailSendUtil;
    private final GenerateRandomStringService generateRandomStringService;

    @GetMapping("/passwordreset")
    public String passwordReset(final Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        return URL_INDEX;
    }

    @PostMapping("/passwordresetresend")
    public String passwordResetResend(final Model model, @ModelAttribute("playerResend") Player playerResend) {

        model.addAttribute("player", new Player());

        if (playerResend == null) {
            model.addAttribute("playerResend", new Player());
            return URL_INDEX;
        }

        // Verify if there is a player with this email.
        Optional<Player> playerOld = playerService.findByEmail(playerResend.getEmail());
        boolean byLogin = false;

        // If there is not exist a player with this email.
        if (playerOld.isEmpty()) {
            playerOld = playerService.findByLogin(playerResend.getEmail());
            byLogin = true;
        }

        // If there is not exist a player with this email/login.
        if (playerOld == null) {
            model.addAttribute("codRegister", 2);
            model.addAttribute("playerResend", playerResend);
            // TODO - pegar msg do messages.
            model.addAttribute("msgRegister", "<br />O login " + playerResend.getEmail() + " não está cadastrado no sistema.");
        } else {
            playerOld.get().setPasswordReset(generateRandomStringService.generate(10));
            playerService.save(playerOld.get());

            if (!byLogin) {
                model.addAttribute("msgRegister",
                        "<br />O código para alterar a senha foi enviado para " + playerOld.get()
                                .getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
                emailSendUtil.sendEmailPasswordReset(playerOld.get());
            } else {
                Player teacher = playerOld.get().getCreator();
                teacher.setName(playerOld.get().getName());
                teacher.setPasswordReset(playerOld.get().getPasswordReset());
                emailSendUtil.sendEmailPasswordReset(teacher);
                model.addAttribute("msgRegister",
                        "<br />O código para alterar a senha foi enviado para " + teacher.getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
            }

            model.addAttribute("codRegister", 1);
            model.addAttribute("playerResend", new Player());
        }

        return URL_INDEX;
    }

    @PostMapping("/passwordreset")
    public String passwordResetSubmit(final Model model, @ModelAttribute("player") Player player) {
        model.addAttribute("playerResend", new Player());

        if (player == null) {
            model.addAttribute("player", new Player());
            return URL_INDEX;
        }

        // Verify if there is a player with this email.
        Optional<Player> playerOld = playerService.findByEmail(player.getEmail());
        boolean byLogin = false;

        // If there is not exist a player with this email.
        if (playerOld.isEmpty()) {
            playerOld = playerService.findByLogin(player.getEmail());
            byLogin = true;
        }

        // If there is not exist a player with this email/login.
        if (playerOld.isEmpty()) {
            model.addAttribute("codRegister", 2);
            model.addAttribute("player", player);
            // TODO - pegar msg do messages.
            model.addAttribute("msgRegister", "<br />O login " + player.getEmail() + " não está cadastrado no sistema.");
        } else {
            // If the code informed is not correct.
            if (!player.getPasswordReset().equals(playerOld.get().getPasswordReset())) {
                model.addAttribute("codRegister", 2);
                model.addAttribute("player", player);
                // TODO - pegar msg do messages.
                model.addAttribute("msgRegister",
                        "<br />O código de confirmação da alteração da senha informado está incorreto. Obs.: caso tenha recebido mais de um e-mail, o código válido é o do último e-mail recebido.");
            } else {
                playerOld.get().setPassword(player.getPassword());
                String msg = playerManagementService.validateChangePasswordPlayer(playerOld.get());

                // If there is no errors.
                if (msg.isEmpty()) {
                    playerOld.get().setPassword(PasswordUtil.encryptPassword(playerOld.get().getPassword()));
                    playerOld.get().setPasswordReset(""); // If the user has changed the password successfully, the reset code is cleaned.
                    playerService.save(playerOld.get());

                    model.addAttribute("codRegister", 1);
                    model.addAttribute("player", new Player());
                    // TODO - pegar msg do messages.
                    if (!byLogin) {
                        model.addAttribute("msgRegister", "<br />A senha de acesso para o login " + playerOld.get().getEmail() + " foi alterada com sucesso!");
                    } else {
                        model.addAttribute("msgRegister", "<br />A senha de acesso para o login " + playerOld.get().getLogin() + " foi alterada com sucesso!");
                    }
                } else {
                    model.addAttribute("codRegister", 2);
                    model.addAttribute("player", player);
                    model.addAttribute("msgRegister", msg);
                }
            }
        }

        return URL_INDEX;
    }
}
