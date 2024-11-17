package com.polifono.service.player;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerGame;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.service.PlayerGameService;
import com.polifono.service.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerCreditService {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final SecurityService securityService;
    private final PlayerService playerService;
    private final PlayerGameService playerGameService;

    /**
     * Verify if the player has enough credits to play the phase.
     * Return true, if the player has credits.
     * This method verify the generic credits and the specific credits for the game passed.
     */
    public boolean playerHasCredits(int playerId, Phase phase) {
        return playerService.findById(playerId)
                .map(player -> player.getCredit() > 0 || player.getPlayerGameList().stream()
                        .anyMatch(pg -> pg.getGame().getId() == phase.getMap().getGame().getId() && pg.getCredit() > 0))
                .orElse(false);
    }

    public Player addCreditsToPlayer(int playerId, int qtdCredits) {
        Optional<Player> player = playerService.findById(playerId);
        return player.map(value -> playerService.save(preparePlayerForAddingCredits(value, qtdCredits))).orElse(null);
    }

    public void addCreditsToPlayerAfterLevelOrGameCompletion(Map map) {
        if (map.isLevelCompleted()) {
            addCreditsToPlayerAfterLevelCompleted();
        } else {
            addCreditsToPlayerAfterGameCompleted();
        }
    }

    public Player removeCreditsFromPlayer(int playerId, int qtdCredits) {
        Optional<Player> player = playerService.findById(playerId);
        return player.map(value -> playerService.save(prepareForRemovingCredits(value, qtdCredits))).orElse(null);
    }

    /**
     * Remove credits from a user.
     * Analyze if the player has specific credits of the game passed.
     * In case affirmative, remove the specific credit from the game.
     * Otherwise, remove a general credit.
     */
    public Player removeOneCreditFromPlayer(Player player, Game game) {
        Optional<PlayerGame> playerGameOpt = player.getPlayerGameList().stream()
                .filter(pg -> pg.getGame().getId() == game.getId() && pg.getCredit() > 0)
                .findFirst();

        if (playerGameOpt.isPresent()) {
            playerGameService.removeCreditsFromPlayer(playerGameOpt.get(), 1);
            return playerService.findById(player.getId()).orElse(null);
        } else {
            return removeCreditsFromPlayer(player.getId(), 1);
        }
    }

    public void updatePlayerAfterPassingPhase(Player player, PlayerPhase playerPhase, Phase currentPhase) {
        player.setScore(player.getScore() + playerPhase.getScore());
        player.setCoin(player.getCoin() + playerPhase.getScore());
        player = removeOneCreditFromPlayer(player, currentPhase.getMap().getGame());
        securityService.updateCurrentAuthenticatedUser(player);
    }

    private Player preparePlayerForAddingCredits(Player player, int qtdCredits) {
        player.setCredit(player.getCredit() + qtdCredits);
        return player;
    }

    private Player prepareForRemovingCredits(Player player, int qtdCredits) {
        // If the player has zero credits, so we don't change that.
        if (player.getCredit() > 0) {
            player.setCredit(player.getCredit() - qtdCredits);
        }

        return player;
    }

    private void addCreditsToPlayerAfterLevelCompleted() {
        Player player = addCreditsToPlayer(securityService.getUserId(), configsCreditsProperties.getLevelCompleted());
        securityService.updateCurrentAuthenticatedUser(player);
    }

    private void addCreditsToPlayerAfterGameCompleted() {
        Player player = addCreditsToPlayer(securityService.getUserId(), configsCreditsProperties.getGameCompleted());
        securityService.updateCurrentAuthenticatedUser(player);
    }
}
