package com.polifono.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.Playervideo;
import com.polifono.domain.enums.Rank;
import com.polifono.domain.enums.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.IPlayerService;
import com.polifono.util.DateUtil;
import com.polifono.util.EmailUtil;
import com.polifono.util.RandomStringUtil;
import com.polifono.util.StringUtil;


@Service
public class PlayerServiceImpl implements IPlayerService {

    private IPlayerRepository repository;

    private IPlayerGameService playerGameService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    public PlayerServiceImpl(IPlayerRepository repository, IPlayerGameService playerGameService) {
        this.repository = repository;
        this.playerGameService = playerGameService;
    }

    public final Player create(Player player) {
        return repository.save(preparePlayerForCreation(player));
    }

    public Player preparePlayerForCreation(Player player) {
        player.setDtInc(new Date());
        player.setActive(true);
        player.setPassword(StringUtil.encryptPassword(player.getPassword()));
        player.setCredit(ConfigsCreditsProperties.getCreation()); // n credits are given to the player when he creates the account.
        player.setRole(Role.USER);
        player.setEmailConfirmed(new RandomStringUtil(10).nextString()); // This field is sent to the player's email to confirm if the email is real.
        return player;
    }

    public final Player save(Player player) {
        return repository.save(player);
    }

    public Optional<Player> findById(int id) {
        return repository.findById(id);
    }

    public final List<Player> findAll() {
        return (List<Player>) repository.findAll();
    }

    public Player findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Player findByEmailAndStatus(String email, boolean status) {
        return repository.findByEmailAndActive(email, status);
    }

    public Player findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public Player findByIdFacebook(Long id) {
        return repository.findByIdFacebook(id);
    }

    /**
     * This method is used to login.
     * This method was changed to permit that an user without email could access the system.
     *
     * @param email
     * @return
     */
    public Optional<Player> findByEmailAndStatusForLogin(String email, boolean status) {
        LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));

        Optional<Player> byEmail = repository.findByEmailAndStatusForLogin(email, status);

        if (byEmail.isPresent()) {
            return byEmail;
        } else {
            // In this case:
            // - Or the email doesn't exist;
            // - Or the player is trying to access the system with his login.
            return repository.findByLoginAndStatusForLogin(email, status);
        }
    }

    @Override
    public List<Player> findByDateIncRange(Date dateBegin, Date dateEnd) {
        return (List<Player>) repository.findByDateIncRange(dateBegin, dateEnd);
    }

    @Override
    public final Player addCreditsToPlayer(int playerId, int qtdCredits) {
        Optional<Player> player = this.findById(playerId);
        return this.save(preparePlayerForAddingCredits(player.get(), qtdCredits));
    }

    public Player preparePlayerForAddingCredits(Player player, int qtdCredits) {
        player.setCredit(player.getCredit() + qtdCredits);
        return player;
    }

    @Override
    public final Player removeCreditsFromPlayer(int playerId, int qtdCredits) {
        Optional<Player> player = this.findById(playerId);
        return this.save(prepareForRemovingCredits(player.get(), qtdCredits));
    }

    public Player prepareForRemovingCredits(Player player, int qtdCredits) {
        // If the player has zero credits, so we don't change that.
        if (player.getCredit() > 0) {
            player.setCredit(player.getCredit() - qtdCredits);
        }

        return player;
    }

    /**
     * Remove credits from an user.
     * Analyze if the player has specific credits of the game passed.
     * In case affirmative, remove the specific credit from the game.
     * Otherwise, remove a general credit.
     *
     * @param player
     * @param game
     * @return
     */
    public Player removeOneCreditFromPlayer(Player player, Game game) {
        boolean hasSpecificCredits = false;
        PlayerGame playerGame = null;

        label:
        {
            for (PlayerGame pg : player.getPlayerGameList()) {
                // If the player has specific credits of this game.
                if ((game.getId() == pg.getGame().getId()) && (pg.getCredit() > 0)) {
                    hasSpecificCredits = true;
                    playerGame = pg;
                    break label;
                }
            }
        }

        if (hasSpecificCredits) {
            playerGameService.removeCreditsFromPlayer(playerGame, 1);
            return this.findById(player.getId()).get();
        } else {
            return this.removeCreditsFromPlayer(player.getId(), 1);
        }
    }

    /**
     * Verify if the player has enough credits to play the phase.
     * Return true, if the player has credits.
     * This method verify the generic credits and the specific credits for the game passed.
     *
     * @param phase
     * @return
     */
    public boolean playerHasCredits(Player player, Phase phase) {
        player = this.findById(player.getId()).get();
        boolean hasCredits = false;

        // If the player has generic credits.
        if (player.getCredit() > 0) {
            hasCredits = true;
        } else {
            label:
            {
                // If the player doesn't have generic credits. Let's see if the player has specific credits.
                for (PlayerGame pg : player.getPlayerGameList()) {
                    // If the player has specific credits of this game.
                    if ((phase.getMap().getGame().getId() == pg.getGame().getId()) && (pg.getCredit() > 0)) {
                        hasCredits = true;
                        break label;
                    }
                }
            }
        }

        return hasCredits;
    }

    /**
     * Verify if the player has already confirmed his e-mail.
     * Return true if the player has already confirmed it. Return false otherwise.
     *
     * @param player
     * @return
     */
    public boolean isEmailConfirmed(Player player) {
        player = this.findById(player.getId()).get();

        if (player.isIndEmailConfirmed()) {
            return true;
        }

        return false;
    }

    /**
     * Verify if the player has all the attributes mandatories when we are creating a new player.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     *
     * @param player
     * @return
     */
    public String validateCreatePlayer(Player player) {
        String msg = "";

        if (player.getName() == null || player.getName().equals("")) {
            msg = msg + "<br />O nome precisa ser informado.";
        } else if (!player.getName().trim().contains(" ")) {
            msg = msg + "<br />Por favor, informe o nome e o sobrenome.";
        }

        if (player.getEmail() == null || player.getEmail().equals("")) {
            msg = msg + "<br />O e-mail precisa ser informado.";
        } else if (!EmailUtil.validateEmail(player.getEmail())) {
            msg = msg + "<br />O e-mail informado não é válido.";
        }

        if (player.getPassword() == null || player.getPassword().equals("")) {
            msg = msg + "<br />A senha precisa ser informada.";
        } else if (player.getPassword().length() < 6 || player.getPassword().length() > 20) {
            msg = msg + "<br />A senha precisa possuir entre 6 e 20 caracteres.";
        } else if (!EmailUtil.validatePassword(player.getPassword())) {
            msg = msg + "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.";
        }

        return msg;
    }

    /**
     * Verify if the player has all the attributes mandatories when the teacher are creating a new player.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     * <p>
     * The difference between this method and the validateCreatePlayer is that here the player has a login and doesn't have an e-mail.
     *
     * @param player
     * @return
     */
    public String validateCreatePlayerByTeacher(Player player) {
        String msg = "";

        if (player.getName() == null || player.getName().equals("")) {
            msg = msg + "<br />O nome precisa ser informado.";
        } else if (!player.getName().trim().contains(" ")) {
            msg = msg + "<br />Por favor, informe o nome e o sobrenome.";
        }

        if (player.getLogin() == null || player.getLogin().equals("")) {
            msg = msg + "<br />O login precisa ser informado.";
        } else if (player.getLogin().length() < 6 || player.getLogin().length() > 20) {
            msg = msg + "<br />O login precisa possuir entre 6 e 20 caracteres.";
        } else if (!EmailUtil.validateLogin(player.getLogin())) {
            msg = msg + "<br />O login só deve possuir letras e números. Não deve possuir espaços, acentos ou demais caracteres especiais.";
        }

        if (player.getPassword() == null || player.getPassword().equals("")) {
            msg = msg + "<br />A senha precisa ser informada.";
        } else if (player.getPassword().length() < 6 || player.getPassword().length() > 20) {
            msg = msg + "<br />A senha precisa possuir entre 6 e 20 caracteres.";
        } else if (!EmailUtil.validatePassword(player.getPassword())) {
            msg = msg + "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.";
        }

        return msg;
    }

    /**
     * Verify if the player has all the attributes mandatories when we are updating a player.
     *
     * @param player
     * @return
     */
    public String validateUpdateProfile(Player player) {
        String msg = "";

        if (player.getName() == null || "".equals(player.getName().trim())) {
            msg = "O nome precisa ser informado.<br />";
        }

        if (player.getLastName() == null || "".equals(player.getLastName().trim())) {
            msg = "O sobrenome precisa ser informado.<br />";
        }

        return msg;
    }

    /**
     * Verify if the player has all the attributes mandatories when he is trying to change his password.
     * If everything is OK, return an empty string.
     * Otherwise, return one string with the message of the error.
     *
     * @param player
     * @return
     */
    public String validateChangePasswordPlayer(Player player) {
        String msg = "";

        if (player.getPassword() == null || player.getPassword().equals("")) {
            msg = msg + "<br />A senha precisa ser informada.";
        } else if (player.getPassword().length() < 6 || player.getPassword().length() > 20) {
            msg = msg + "<br />A senha precisa possuir entre 6 e 20 caracteres.";
        } else if (!EmailUtil.validatePassword(player.getPassword())) {
            msg = msg + "<br />A senha precisa possuir ao menos 1 número e ao menos 1 letra.";
        }

        return msg;
    }

    @Override
    public String validateAddVideo(Playervideo playervideo) {
        String msg = "";

        if (playervideo.getPlayer().getRankLevel() <= Rank.WHITE.getLevel()) {
            return "<br />Você ainda não tem permissão para adicionar vídeos.<br />Continue estudando para desbloquear essa funcionalidade!";
        } else if (playervideo.getContent() == null || playervideo.getContent().getPhase() == null || playervideo.getContent().getPhase().getId() == 0) {
            msg = msg + "<br />Por favor, selecione uma fase.";
        } else if ("".equals(StringUtil.formatYoutubeUrl(playervideo.getUrl()))) {
            msg = msg + "<br />O endereço do vídeo informado não parece estar correto.";
        }

        return msg;
    }

    @Override
    public List<Player> findCommunicationGroup04() {
        return repository.findCommunicationGroup04(DateUtil.subtractMonth(new Date(), 1), DateUtil.subtractMonth(new Date(), 2));
    }

    @Override
    public List<Player> findCommunicationGroup05() {
        return repository.findCommunicationGroup05(DateUtil.subtractMonth(new Date(), 1), DateUtil.subtractMonth(new Date(), 2));
    }
}
