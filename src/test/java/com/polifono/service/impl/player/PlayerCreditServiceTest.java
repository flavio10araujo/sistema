package com.polifono.service.impl.player;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerGame;
import com.polifono.model.enums.Role;
import com.polifono.service.IPlayerGameService;

@ExtendWith(MockitoExtension.class)
public class PlayerCreditServiceTest {

    @InjectMocks
    private PlayerCreditService service;

    @Mock
    private PlayerService playerService;

    @Mock
    private IPlayerGameService playerGameService;

    private final Integer PLAYER_ID_EXISTENT = 1;

    private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";

    private static final Integer GAME_ID_EXISTENT = 1;

    /* playerHasCredits - begin */
    @Test
    public void playerHasCredits_WhenPlayerHasGenericCredits_ReturnTrue() {
        int id = PLAYER_ID_EXISTENT;

        Player player = new Player();
        player.setId(id);
        player.setCredit(30);

        Phase phase = new Phase();

        when(playerService.findById(id)).thenReturn(Optional.of(player));

        Assertions.assertTrue(service.playerHasCredits(player.getId(), phase));
    }

    @Test
    public void playerHasCredits_WhenPlayerHasSpecificCredits_ReturnTrue() {
        Player entity = getPlayerWithSpecificCreditsStubData();
        entity.setCredit(0); // Assuring that the player has no generic credits.

        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Assertions.assertTrue(service.playerHasCredits(entity.getId(), getPhaseStubData()));

        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void playerHasCredits_WhenPlayerHasGenericAndSpecificCredits_ReturnTrue() {
        Player entity = getPlayerWithSpecificCreditsStubData();
        entity.setCredit(30); // Assuring that the player has generic credits.

        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Assertions.assertTrue(service.playerHasCredits(entity.getId(), getPhaseStubData()));

        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void playerHasCredits_WhenPlayerHasNotCredits_ReturnFalse() {
        Optional<Player> entity = getEntityStubData();
        entity.get().setCredit(0); // Assuring that the player has no generic credits.

        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);

        Assertions.assertFalse(service.playerHasCredits(entity.get().getId(), getPhaseStubData()));

        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(playerService);
    }
    /* playerHasCredits - end */

    /* addCreditsToPlayer - begin */
    @Test
    public void addCreditsToPlayer_WhenEverythingIsOK_ReturnPlayerWithMoreCredits() {
        Optional<Player> entity = getEntityStubData();

        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);
        when(playerService.save(entity.get())).thenReturn(entity.get());

        Player entityReturned = service.addCreditsToPlayer(PLAYER_ID_EXISTENT, 10);

        Assertions.assertNotNull(entityReturned, "failure - expected null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verify(playerService, times(1)).save(entity.get());
        verifyNoMoreInteractions(playerService);
    }
    /* addCreditsToPlayer - end */

    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenEverythingIsOK_ReturnPlayerWithLessCredits() {
        Optional<Player> entity = getEntityStubData();

        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);
        when(playerService.save(entity.get())).thenReturn(entity.get());

        Player entityReturned = service.removeCreditsFromPlayer(PLAYER_ID_EXISTENT, 5);

        Assertions.assertNotNull(entityReturned, "failure - expected null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verify(playerService, times(1)).save(entity.get());
        verifyNoMoreInteractions(playerService);
    }
    /* removeCreditsFromPlayer - end */

    /* removeOneCreditFromPlayer - begin */
    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerHasSpecificCreditsOfTheGame_ReturnPlayerWithOneSpecificCreditLess() {
        Player entity = getPlayerWithSpecificCreditsStubData();

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        PlayerGame playerGame = entity.getPlayerGameList().get(0);

        when(playerGameService.removeCreditsFromPlayer(playerGame, 1)).thenReturn(playerGame);
        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(Optional.of(entity));

        Player entityReturned = service.removeOneCreditFromPlayer(entity, game);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(playerGameService, times(1)).removeCreditsFromPlayer(playerGame, 1);
        verifyNoMoreInteractions(playerGameService);
        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void removeOneCreditFromPlayer_WhenThePlayerDoesntHaveSpecificCreditsOfTheGame_ReturnPlayerWithOneGenericCreditLess() {
        Optional<Player> entity = getEntityStubData();

        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        when(playerService.findById(PLAYER_ID_EXISTENT)).thenReturn(entity);
        when(playerService.save(entity.get())).thenReturn(entity.get());

        Player entityReturned = service.removeOneCreditFromPlayer(entity.get(), game);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(playerService, times(1)).findById(PLAYER_ID_EXISTENT);
        verify(playerService, times(1)).save(entity.get());
        verifyNoMoreInteractions(playerService);
    }
    /* removeOneCreditFromPlayer - end */

    /* stubs - begin */
    private Player getPlayerWithSpecificCreditsStubData() {
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);

        List<PlayerGame> playerGameList = new ArrayList<>();

        PlayerGame playerGame = new PlayerGame();
        playerGame.setCredit(20);
        playerGame.setGame(game);

        playerGameList.add(playerGame);

        Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);
        player.setCredit(0); // With no generic credits.
        player.setPlayerGameList(playerGameList); // With specific credits.

        return player;
    }

    private Phase getPhaseStubData() {
        Phase phase = new Phase();
        Map map = new Map();
        Game game = new Game();
        game.setId(GAME_ID_EXISTENT);
        map.setGame(game);
        phase.setMap(map);

        return phase;
    }

    private Optional<Player> getEntityStubData() {
        List<PlayerGame> playerGameList = new ArrayList<>();

        Player entity = new Player();
        entity.setId(PLAYER_ID_EXISTENT);
        entity.setName("Name of the Player");
        entity.setEmail(PLAYER_EMAIL_EXISTENT);
        entity.setPassword("password");
        entity.setRole(Role.USER);

        entity.setPlayerGameList(playerGameList);

        return Optional.of(entity);
    }
    /* stubs - end */
}