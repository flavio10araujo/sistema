package com.polifono.service.impl.player;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.PlayerPhase;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.impl.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerCreditService {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final SecurityService securityService;
    private final PlayerService playerService;
    private final IPlayerGameService playerGameService;

    /**
     * Verify if the player has enough credits to play the phase.
     * Return true, if the player has credits.
     * This method verify the generic credits and the specific credits for the game passed.
     */
    public boolean playerHasCredits(int playerId, Phase phase) {
        Player player = playerService.findById(playerId).get();
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
