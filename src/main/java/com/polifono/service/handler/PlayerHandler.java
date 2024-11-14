package com.polifono.service.handler;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.polifono.common.util.EmailUtil;
import com.polifono.common.util.PlayerUtil;
import com.polifono.common.util.YouTubeUrlFormatter;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.Playervideo;
import com.polifono.model.enums.Rank;
import com.polifono.service.impl.player.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerHandler {

    private final MessageSource messagesResource;
    private final PlayerService playerService;

    /**
     * Verify if the player has all the attributes mandatories when we are creating a new player.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     */
    public String validateCreatePlayer(Player player) {
        String msg = "";

        if (player.getName() == null || player.getName().isEmpty()) {
            msg = msg + "<br />O nome precisa ser informado.";
        } else if (!player.getName().trim().contains(" ")) {
            msg = msg + "<br />Por favor, informe o nome e o sobrenome.";
        }

        if (player.getEmail() == null || player.getEmail().isEmpty()) {
            msg = msg + "<br />O e-mail precisa ser informado.";
        } else if (!EmailUtil.validateEmail(player.getEmail())) {
            msg = msg + "<br />O e-mail informado não é válido.";
        }

        msg = validatePassword(player, msg);

        return msg;
    }

    /**
     * Verify if the player has all the mandatory attributes when the teacher are creating a new player.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     * <p>
     * The difference between this method and the validateCreatePlayer is that here the player has a login and doesn't have an e-mail.
     */
    public String validateCreatePlayerByTeacher(Player player) {
        String msg = "";

        if (player.getName() == null || player.getName().isEmpty()) {
            msg = msg + "<br />O nome precisa ser informado.";
        } else if (!player.getName().trim().contains(" ")) {
            msg = msg + "<br />Por favor, informe o nome e o sobrenome.";
        }

        if (player.getLogin() == null || player.getLogin().isEmpty()) {
            msg = msg + "<br />O login precisa ser informado.";
        } else if (player.getLogin().length() < 6 || player.getLogin().length() > 20) {
            msg = msg + "<br />O login precisa possuir entre 6 e 20 caracteres.";
        } else if (!EmailUtil.validateLogin(player.getLogin())) {
            msg = msg + "<br />O login só deve possuir letras e números. Não deve possuir espaços, acentos ou demais caracteres especiais.";
        }

        msg = validatePassword(player, msg);

        return msg;
    }

    /**
     * Verify if the player has all the mandatory attributes when we are updating a player.
     */
    // TODO - i18n
    public String validateUpdateProfile(Player player) {
        String msg = "";

        if (player.getName() == null || player.getName().trim().isEmpty()) {
            msg = "O nome precisa ser informado.<br />";
        }

        if (player.getLastName() == null || player.getLastName().trim().isEmpty()) {
            msg = "O sobrenome precisa ser informado.<br />";
        }

        return msg;
    }

    /**
     * Verify if the player has all the mandatory attributes when he is trying to change his password.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     */
    public String validateChangePasswordPlayer(Player player) {
        String msg = "";
        msg = validatePassword(player, msg);
        return msg;
    }

    // TODO - i18n
    public String validateAddVideo(Playervideo playervideo) {
        String msg = "";

        if (playervideo.getPlayer().getRankLevel() <= Rank.WHITE.getLevel()) {
            return "<br />Você ainda não tem permissão para adicionar vídeos.<br />Continue estudando para desbloquear essa funcionalidade!";
        } else if (playervideo.getContent() == null || playervideo.getContent().getPhase() == null || playervideo.getContent().getPhase().getId() == 0) {
            msg = msg + "<br />Por favor, selecione uma fase.";
        } else if (YouTubeUrlFormatter.formatUrl(playervideo.getUrl()).isEmpty()) {
            msg = msg + "<br />O endereço do vídeo informado não parece estar correto.";
        }

        return msg;
    }

    // TODO - i18n
    private String validatePassword(Player player, String msg) {
        if (player.getPassword() == null || player.getPassword().isEmpty()) {
            msg = msg + "<br />A senha precisa ser informada.";
        } else if (player.getPassword().length() < 6 || player.getPassword().length() > 20) {
            msg = msg + "<br />A senha precisa possuir entre 6 e 20 caracteres.";
        } else if (!EmailUtil.validatePassword(player.getPassword())) {
            msg = msg + "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.";
        }

        return msg;
    }

    public String validateAndSanitizeEmail(Player player, Locale locale) {
        if (player.getEmail() == null || player.getEmail().trim().isEmpty()) {
            return null;
        }

        player.setEmail(EmailUtil.avoidWrongDomain(player.getEmail()));
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
}