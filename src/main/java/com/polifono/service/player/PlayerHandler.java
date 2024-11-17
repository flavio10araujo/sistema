package com.polifono.service.player;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.polifono.common.util.EmailDomainCorrector;
import com.polifono.common.util.EmailValidator;
import com.polifono.common.util.LoginValidator;
import com.polifono.common.util.PasswordValidator;
import com.polifono.common.util.PlayerUtil;
import com.polifono.common.util.YouTubeUrlFormatter;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.Playervideo;
import com.polifono.model.enums.Rank;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerHandler {

    private final MessageSource messagesResource;
    private final PlayerService playerService;

    /**
     * Verify if the player has all the attributes mandatory when we are creating a new player.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     */
    public String validateCreatePlayer(Player player, Locale locale) {
        String msg = "";
        msg = validateFullName(player.getName(), msg, locale);
        msg = validateEmail(player.getEmail(), msg, locale);
        msg = validatePassword(player, msg, locale);
        return msg;
    }

    /**
     * Verify if the player has all the mandatory attributes when the teacher are creating a new player.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     * <p>
     * The difference between this method and the validateCreatePlayer is that here the player has a login and doesn't have an e-mail.
     */
    public String validateCreatePlayerByTeacher(Player player, Locale locale) {
        String msg = "";
        msg = validateFullName(player.getName(), msg, locale);
        msg = validateLogin(player.getLogin(), msg, locale);
        msg = validatePassword(player, msg, locale);
        return msg;
    }

    /**
     * Verify if the player has all the mandatory attributes when we are updating a player.
     */
    public String validateUpdateProfile(Player player, Locale locale) {
        String msg = "";
        msg = validateFirstName(player.getName(), msg, locale);
        msg = validateLastName(player.getLastName(), msg, locale);
        return msg;
    }

    /**
     * Verify if the player has all the mandatory attributes when he is trying to change his password.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     */
    public String validateChangePasswordPlayer(Player player, Locale locale) {
        return validatePassword(player, "", locale);
    }

    public String validateAddVideo(Playervideo playervideo, Locale locale) {
        if (playervideo.getPlayer().getRankLevel() <= Rank.WHITE.getLevel()) {
            return "<br />" + messagesResource.getMessage("msg.playerProfile.addVideo.notAllowed.noPermission", null, locale);
        }

        if (playervideo.getContent() == null || playervideo.getContent().getPhase() == null || playervideo.getContent().getPhase().getId() == 0) {
            return "<br />" + messagesResource.getMessage("msg.playerProfile.addVideo.notAllowed.noPhase", null, locale);
        }

        if (YouTubeUrlFormatter.formatUrl(playervideo.getUrl()).isEmpty()) {
            return "<br />" + messagesResource.getMessage("msg.playerProfile.addVideo.notAllowed.invalidUrl", null, locale);
        }

        return "";
    }

    public String validateAndSanitizeEmail(Player player, Locale locale) {
        if (player.getEmail() == null || player.getEmail().trim().isEmpty()) {
            return null;
        }

        player.setEmail(EmailDomainCorrector.correctDomain(player.getEmail()));
        Optional<Player> playerOpt = playerService.findByEmail(player.getEmail());

        if (playerOpt.isPresent()) {
            return messagesResource.getMessage("msg.register.emailAlreadyExists", new Object[] { player.getEmail() }, locale);
        }

        return null;
    }

    public void formatPlayerNames(Player player) {
        String formattedName = PlayerUtil.formatNamePlayer(player.getName());
        player.setName(getFirstName(formattedName));
        player.setLastName(getLastName(formattedName));
    }

    private String getFirstName(String name) {
        return name.contains(" ") ? name.substring(0, name.indexOf(" ")).trim() : name;
    }

    private String getLastName(String name) {
        return name.contains(" ") ? name.substring(name.indexOf(" ") + 1).trim() : "";
    }

    private String validateFullName(String name, String msg, Locale locale) {
        if (name == null || name.isEmpty()) {
            msg += "<br />" + messagesResource.getMessage("msg.register.missingName", null, locale);
        } else if (!name.trim().contains(" ")) {
            msg += "<br />" + messagesResource.getMessage("msg.register.missingFullName", null, locale);
        }
        return msg;
    }

    private String validateFirstName(String name, String msg, Locale locale) {
        if (name == null || name.isEmpty()) {
            msg += "<br />" + messagesResource.getMessage("msg.register.missingName", null, locale);
        }
        return msg;
    }

    private String validateLastName(String lastName, String msg, Locale locale) {
        if (lastName == null || lastName.trim().isEmpty()) {
            msg += messagesResource.getMessage("msg.register.missingLastName", null, locale) + "<br />";
        }
        return msg;
    }

    private String validateEmail(String email, String msg, Locale locale) {
        if (email == null || email.isEmpty()) {
            msg += "<br />" + messagesResource.getMessage("msg.register.missingEmail", null, locale);
        } else if (!EmailValidator.isValid(email)) {
            msg += "<br />" + messagesResource.getMessage("msg.register.invalidEmail", null, locale);
        }
        return msg;
    }

    private String validateLogin(String login, String msg, Locale locale) {
        if (login == null || login.isEmpty()) {
            msg += "<br />" + messagesResource.getMessage("msg.register.missingLogin", null, locale);
        } else if (login.length() < 6 || login.length() > 20) {
            msg += "<br />" + messagesResource.getMessage("msg.register.invalidLogin.size", null, locale);
        } else if (!LoginValidator.isValid(login)) {
            msg += "<br />" + messagesResource.getMessage("msg.register.invalidLogin.pattern", null, locale);
        }
        return msg;
    }

    private String validatePassword(Player player, String msg, Locale locale) {
        if (player.getPassword() == null || player.getPassword().isEmpty()) {
            msg += "<br />" + messagesResource.getMessage("msg.register.missingPassword", null, locale);
        } else if (player.getPassword().length() < 6 || player.getPassword().length() > 20) {
            msg += "<br />" + messagesResource.getMessage("msg.register.invalidPassword.size", null, locale);
        } else if (!PasswordValidator.isValid(player.getPassword())) {
            msg += "<br />" + messagesResource.getMessage("msg.register.invalidPassword.pattern", null, locale);
        }
        return msg;
    }
}
