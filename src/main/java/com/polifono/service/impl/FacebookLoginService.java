package com.polifono.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.polifono.common.util.UrlReaderUtil;
import com.polifono.model.PlayerFacebook;
import com.polifono.model.entity.Player;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.impl.player.PlayerService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FacebookLoginService {

    public static final String GRAPH_FB_URL = "https://graph.facebook.com/v2.12/me?fields=email,first_name,last_name&access_token=";

    private final IPlayerRepository repository;
    private final SecurityService securityService;
    private final GenerateRandomStringService generateRandomStringService;
    private final LoginServiceImpl loginService;
    private final PlayerService playerService;

    public Optional<Player> findByIdFacebook(Long id) {
        return repository.findByIdFacebook(id);
    }

    public PlayerFacebook getPlayerFacebookFromCode(String code) throws IOException {
        JSONObject resp = new JSONObject(UrlReaderUtil.readURL(new URL(GRAPH_FB_URL + code)));
        return new PlayerFacebook(resp);
    }

    public void loginExistingPlayer(HttpServletRequest request, Player player) {
        request.getSession(true);
        securityService.createCurrentAuthenticatedUserFacebook(request, null, player);
        loginService.registerLogin(player);
    }

    public void handleNewFacebookLogin(HttpServletRequest request, PlayerFacebook playerFacebook) {
        if (hasValidEmail(playerFacebook)) {
            Optional<Player> playerOpt = playerService.findByEmail(playerFacebook.getEmail());
            if (playerOpt.isPresent()) {
                // If it is here it is because the playerFacebook's email is already registered in the system, but it is not linked to any Facebook account.
                // Let's create the link and register in the database.
                linkFacebookAccountToExistingPlayer(request, playerOpt.get(), playerFacebook);
            } else {
                registerNewPlayer(request, playerFacebook);
            }
        } else {
            // If it is here it is because the playerFacebook doesn't exist in the system AND doesn't have an email.
            // In this case, the user will not have neither an email nor login. He will always log in with his Facebook account.
            registerNewPlayerWithoutEmail(request, playerFacebook);
        }
    }

    private boolean hasValidEmail(PlayerFacebook playerFacebook) {
        return playerFacebook.getEmail() != null && !playerFacebook.getEmail().isEmpty();
    }

    private void linkFacebookAccountToExistingPlayer(HttpServletRequest request, Player player, PlayerFacebook playerFacebook) {
        player.setIdFacebook(playerFacebook.getId());
        player.setIndEmailConfirmed(true);
        playerService.save(player);
        loginExistingPlayer(request, player);
    }

    private void registerNewPlayer(HttpServletRequest request, PlayerFacebook playerFacebook) {
        Player player = new Player();
        player.setIdFacebook(playerFacebook.getId());
        player.setName(playerFacebook.getFirstName());
        player.setLastName(playerFacebook.getLastName());
        player.setEmail(playerFacebook.getEmail());
        player.setPassword(generateRandomStringService.generate(6));
        player.setIndEmailConfirmed(false);
        playerService.create(player);
        loginExistingPlayer(request, player);
    }

    private void registerNewPlayerWithoutEmail(HttpServletRequest request, PlayerFacebook playerFacebook) {
        Player player = new Player();
        player.setIdFacebook(playerFacebook.getId());
        player.setName(playerFacebook.getFirstName());
        player.setLastName(playerFacebook.getLastName());
        player.setLogin(playerFacebook.getId() + "");
        player.setPassword(generateRandomStringService.generate(6));
        player.setIndEmailConfirmed(false);
        playerService.create(player);
        loginExistingPlayer(request, player);
    }
}
