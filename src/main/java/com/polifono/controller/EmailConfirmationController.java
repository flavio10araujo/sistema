package com.polifono.controller;

import static com.polifono.common.TemplateConstants.URL_EMAIL_CONFIRMATION;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.polifono.domain.Player;
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EmailConfirmationController {

    private final PlayerService playerService;
    private final SendEmailService emailSendUtil;
    private final GenerateRandomStringService generateRandomStringService;

    @GetMapping("/emailconfirmation")
    public String emailConfirmation(final Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        return URL_EMAIL_CONFIRMATION;
    }

    @PostMapping("/emailconfirmation")
    public String emailConfirmationSubmit(final Model model,
            @ModelAttribute("player") Player player) {

        model.addAttribute("playerResend", new Player());

        if (player == null) {
            model.addAttribute("player", new Player());
            return URL_EMAIL_CONFIRMATION;
        }

        // Verify if there is a player with this email.
        Optional<Player> playerOldOpt = playerService.findByEmail(player.getEmail());

        // If there is not exist a player with this email.
        if (playerOldOpt.isEmpty()) {
            model.addAttribute("codRegister", 2);
            model.addAttribute("player", player);
            // TODO - pegar msg do messages.
            model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " não está cadastrado no sistema.");
        } else {
            // If the player was already confirmed.
            if (playerOldOpt.get().isIndEmailConfirmed()) {
                model.addAttribute("codRegister", 2);
                model.addAttribute("player", player);
                // TODO - pegar msg do messages.
                model.addAttribute("msgRegister", "<br />Este cadastro já se encontra ativo.");
            } else {
                // If the code informed is not correct.
                if (!player.getEmailConfirmed().equals(playerOldOpt.get().getEmailConfirmed())) {
                    model.addAttribute("codRegister", 2);
                    model.addAttribute("player", player);
                    // TODO - pegar msg do messages.
                    model.addAttribute("msgRegister",
                            "<br />O código de ativação informado está incorreto. Obs.: caso tenha recebido mais de um e-mail, o código válido é o do último e-mail recebido.");
                } else {
                    playerOldOpt.get().setIndEmailConfirmed(true);
                    playerService.save(playerOldOpt.get());

                    model.addAttribute("codRegister", 1);
                    model.addAttribute("player", new Player());
                    // TODO - pegar msg do messages.
                    model.addAttribute("msgRegister", "<br />O e-mail " + playerOldOpt.get().getEmail() + " foi confirmado com sucesso!");
                }
            }
        }

        return URL_EMAIL_CONFIRMATION;
    }

    @PostMapping("/emailconfirmationresend")
    public String emailConfirmationResend(final Model model, @ModelAttribute("playerResend") Player playerResend) {

        model.addAttribute("player", new Player());

        if (playerResend == null) {
            model.addAttribute("playerResend", new Player());
            return URL_EMAIL_CONFIRMATION;
        }

        // Verify if there is a player with this email.
        Optional<Player> playerOld = playerService.findByEmail(playerResend.getEmail());

        // If there is not exist a player with this email.
        if (playerOld.isEmpty()) {
            model.addAttribute("codRegister", 2);
            model.addAttribute("playerResend", playerResend);
            // TODO - pegar msg do messages.
            model.addAttribute("msgRegister", "<br />O email " + playerResend.getEmail() + " não está cadastrado no sistema.");
        } else {
            // If the player was already confirmed.
            if (playerOld.get().isIndEmailConfirmed()) {
                model.addAttribute("codRegister", 2);
                model.addAttribute("playerResend", playerResend);
                // TODO - pegar msg do messages.
                model.addAttribute("msgRegister", "<br />Este cadastro já se encontra ativo.");
            } else {
                playerOld.get().setEmailConfirmed(generateRandomStringService.generate(10));
                playerService.save(playerOld.get());
                emailSendUtil.sendEmailConfirmRegister(playerOld.get());

                model.addAttribute("codRegister", 1);
                model.addAttribute("playerResend", new Player());
                // TODO - pegar msg do messages.
                model.addAttribute("msgRegister",
                        "<br />O e-mail com o código de ativação foi reenviado para " + playerOld.get()
                                .getEmail() + ". <br />Obs.: o e-mail leva alguns minutos para chegar. Verifique se o e-mail não está na caixa de spam.");
            }
        }

        return URL_EMAIL_CONFIRMATION;
    }
}
