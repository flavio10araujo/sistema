package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_INDEX;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.polifono.domain.Player;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerManagementService;
import com.polifono.service.impl.player.PlayerService;
import com.polifono.util.EmailUtil;
import com.polifono.util.PlayerUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerManagementService playerManagementService;
    private final SendEmailService emailSendUtil;

    @PostMapping("/players")
    public synchronized String createPlayer(HttpServletRequest request, final Model model, @ModelAttribute("player") Player player) {

        model.addAttribute("playerResend", new Player());

        if (player == null) {
            log.debug("/players POST player is null");
            model.addAttribute("player", new Player());
            return URL_INDEX;
        }

        // Verify if the email is already in use.
        Optional<Player> playerOld = null;

        if (player.getEmail() != null && !player.getEmail().trim().isEmpty()) {
            player.setEmail(EmailUtil.avoidWrongDomain(player.getEmail()));
            playerOld = playerService.findByEmail(player.getEmail());
        }

        if (playerOld.isPresent()) {
            model.addAttribute("player", player);
            model.addAttribute("codRegister", 2);
            // TODO - buscar msg do messages.
            model.addAttribute("msgRegister", "<br />O email " + player.getEmail() + " já está cadastrado para outra pessoa.");
        } else {
            String msg = playerManagementService.validateCreatePlayer(player);

            // If there is not errors.
            if (msg.isEmpty()) {
                String password = player.getPassword();

                player.setName(PlayerUtil.formatNamePlayer(player.getName()));

                String name = player.getName();
                name = name.substring(0, name.indexOf(" "));

                String lastName = player.getName();
                lastName = lastName.substring(lastName.indexOf(" ") + 1).trim();

                player.setLastName(lastName);
                player.setName(name);

                model.addAttribute("player", playerService.create(player));
                model.addAttribute("codRegister", 1);
                emailSendUtil.sendEmailConfirmRegister(player);

                try {
                    request.login(player.getEmail(), password);
                } catch (ServletException e) {
                    log.error("Error in the login of the player {} ", player.getEmail());
                }

                return REDIRECT_HOME;
            } else {
                model.addAttribute("player", player);
                model.addAttribute("codRegister", 2);
                model.addAttribute("msgRegister", msg);
            }
        }

        return URL_INDEX;
    }
}
