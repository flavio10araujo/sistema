package com.polifono.controller;

import static com.polifono.common.constant.TemplateConstants.REDIRECT_HOME;
import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.polifono.model.PlayerFacebook;
import com.polifono.model.entity.Player;
import com.polifono.service.FacebookLoginService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private static final int CODE_REGISTER_ERROR = 2;

    private final MessageSource messagesResource;
    private final FacebookLoginService facebookLoginService;

    @GetMapping("/login")
    public String login(final Model model, @RequestParam Optional<String> error) {
        prepareModel(model, error);
        return URL_INDEX;
    }

    /**
     * Method used when the user does the login with his Facebook account.
     */
    @RequestMapping("/loginfb")
    public synchronized String loginFacebook(HttpServletRequest request,
            final Model model,
            String code,
            Locale locale) {
        try {
            PlayerFacebook playerFacebook = facebookLoginService.getPlayerFacebookFromCode(code);
            if (playerFacebook.getId() == null) {
                return URL_INDEX;
            }

            // Get the ID of the playerFacebook and verify if he is already related to any player.
            Optional<Player> playerOpt = facebookLoginService.findByIdFacebook(playerFacebook.getId());

            // If yes, log the player in.
            if (playerOpt.isPresent()) {
                facebookLoginService.loginExistingPlayer(request, playerOpt.get());
            } else {
                facebookLoginService.handleNewFacebookLogin(request, playerFacebook);
            }

            return REDIRECT_HOME;
        } catch (IOException e) {
            return handleLoginError(model, locale);
        }
    }

    private void prepareModel(Model model, Optional<String> error) {
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        model.addAttribute("error", error);
    }

    private String handleLoginError(Model model, Locale locale) {
        model.addAttribute("player", new Player());
        model.addAttribute("codRegister", CODE_REGISTER_ERROR);
        model.addAttribute("msgRegister", "<br />" + messagesResource.getMessage("msg.loginFB.nOk", null, locale));
        return URL_INDEX;
    }
}
