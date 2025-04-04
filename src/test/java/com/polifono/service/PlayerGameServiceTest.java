package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.entity.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;

/**
 * Unit test methods for the PlayerGameService.
 */
@ExtendWith(MockitoExtension.class)
public class PlayerGameServiceTest {

    @InjectMocks
    private PlayerGameService service;

    @Mock
    private IPlayerGameRepository repository;

    private final Integer PLAYER_GAME_ID_EXISTENT = 1;
    private final Integer PLAYER_GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    /* findOne - begin */
    @Test
    public void findOne_WhenPlayerGameIsExistent_ReturnAnswer() {
        Optional<PlayerGame> entity = getEntityStubData();

        when(repository.findById(PLAYER_GAME_ID_EXISTENT)).thenReturn(entity);

        Optional<PlayerGame> entityReturned = service.findById(PLAYER_GAME_ID_EXISTENT);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_GAME_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(PLAYER_GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenPlayerGameIsInexistent_ReturnNull() {
        when(repository.findById(PLAYER_GAME_ID_INEXISTENT)).thenReturn(null);

        Optional<PlayerGame> entityReturned = service.findById(PLAYER_GAME_ID_INEXISTENT);

        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(PLAYER_GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenRemoveOneCredit_ReturnPlayerWithOneCreditLess() {
        Optional<PlayerGame> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        PlayerGame entityReturned = service.removeCreditsFromPlayer(entity.get(), 1);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYER_GAME_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* removeCreditsFromPlayer - end */

    /* stubs - begin */
    private Optional<PlayerGame> getEntityStubData() {
        PlayerGame playerGame = new PlayerGame();
        playerGame.setId(PLAYER_GAME_ID_EXISTENT);

        return Optional.of(playerGame);
    }
    /* stubs - end */
}
