package com.polifono.controller;

import static com.polifono.common.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.TemplateConstants.URL_INDEX;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.domain.Player;
import com.polifono.domain.bean.PlayerFacebook;
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.LoginServiceImpl;
import com.polifono.service.impl.SecurityService;
import com.polifono.service.impl.player.PlayerService;
import com.polifono.util.UrlReaderUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final SecurityService securityService;
    private final PlayerService playerService;
    private final LoginServiceImpl loginService;
    private final GenerateRandomStringService generateRandomStringService;

    @GetMapping("/login")
    public String login(final Model model, @RequestParam Optional<String> error) {
        log.debug("Getting login page, error={}", error);
        prepareModel(model, error);
        return URL_INDEX;
    }

    /**
     * Method used when the user does the login with his Facebook account.
     */
    @RequestMapping("/loginfb")
    public synchronized String loginfb(HttpServletRequest request, final Model model, String code) {
        try {
            JSONObject resp = new JSONObject(
                    UrlReaderUtil.readURL(new URL("https://graph.facebook.com/v2.12/me?fields=email,first_name,last_name&access_token=" + code)));
            PlayerFacebook playerFacebook = new PlayerFacebook(resp);

            if (playerFacebook.getId() == null) {
                log.warn("loginFb - playerFacebook is null");
                return URL_INDEX;
            }

            // Get the ID of the playerFacebook and verify if he is already related to any player.
            Optional<Player> playerOpt = playerService.findByIdFacebook(playerFacebook.getId());

            // If yes, log the player in.
            if (playerOpt.isPresent()) {
                request.getSession(true);
                securityService.createCurrentAuthenticatedUserFacebook(request, null, playerOpt.get());
                loginService.registerLogin(playerOpt.get());
            } else {
                // If not, verify if the playerFacebook has an email.
                if (playerFacebook.getEmail() != null && !playerFacebook.getEmail().equals("")) {

                    // If it is here it is because playerFacebook doesn't exist in the system AND has an email.

                    playerOpt = playerService.findByEmail(playerFacebook.getEmail());

                    // If the playerFacebook's email is already registered in the system.
                    if (playerOpt.isPresent()) {
                        // If it is here it is because the playerFacebook's email is already registered in the system, but it is not linked to any Facebook account.
                        // Let's create the link and register in the database.
                        playerOpt.get().setIdFacebook(playerFacebook.getId());
                        playerOpt.get().setIndEmailConfirmed(true);

                        playerService.save(playerOpt.get());

                        request.getSession(true);
                        securityService.createCurrentAuthenticatedUserFacebook(request, null, playerOpt.get());
                        loginService.registerLogin(playerOpt.get());
                    } else {
                        // If it is here it is because it is necessary to register the player in the system.
                        Player player = new Player();

                        player.setIdFacebook(playerFacebook.getId());
                        player.setName(playerFacebook.getFirstName());
                        player.setLastName(playerFacebook.getLastName());
                        player.setEmail(playerFacebook.getEmail());
                        player.setPassword(generateRandomStringService.generate(6));
                        player.setIndEmailConfirmed(false);

                        playerService.create(player);

                        request.getSession(true);
                        securityService.createCurrentAuthenticatedUserFacebook(request, null, player);
                        loginService.registerLogin(player);
                    }
                } else {
                    // If it is here it is because the playerFacebook doesn't exist in the system AND E doesn't have an email.
                    // In this case, the user will not have neither an email nor login. He will always log in with his Facebook account.

                    Player player = new Player();

                    player.setIdFacebook(playerFacebook.getId());
                    player.setName(playerFacebook.getFirstName());
                    player.setLastName(playerFacebook.getLastName());
                    player.setPassword(generateRandomStringService.generate(6));
                    player.setLogin(playerFacebook.getId() + "");
                    player.setIndEmailConfirmed(false);

                    playerService.create(player);

                    request.getSession(true);
                    securityService.createCurrentAuthenticatedUserFacebook(request, null, player);
                    loginService.registerLogin(player);
                }
            }

            return REDIRECT_HOME;
        } catch (MalformedURLException e) {
            model.addAttribute("player", new Player());
            model.addAttribute("codRegister", 2);
            // TODO - buscar msg do messages.
            model.addAttribute("msgRegister", "<br />Ocorreu algum erro ao utilizar sua conta do Facebook.");
            System.out.println("MalformedURLException - loginfb - Ocorreu algum erro ao utilizar sua conta do Facebook.");
            return URL_INDEX;
        } catch (IOException e) {
            model.addAttribute("player", new Player());
            model.addAttribute("codRegister", 2);
            // TODO - buscar msg do messages.
            model.addAttribute("msgRegister", "<br />Algum erro ocorreu ao utilizar sua conta do Facebook.");
            System.out.println("IOException - loginfb - Ocorreu algum erro ao utilizar sua conta do Facebook.");
            return URL_INDEX;
        }
    }

    private void prepareModel(Model model, Optional<String> error) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        model.addAttribute("error", error);
    }
}
