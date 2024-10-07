package com.polifono.service.impl;

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

import com.polifono.domain.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;

/**
 * Unit test methods for the PlayerGameService.
 */
@ExtendWith(MockitoExtension.class)
public class PlayerGameServiceImplTest {

    @InjectMocks
    private PlayerGameServiceImpl service;

    @Mock
    private IPlayerGameRepository repository;

    private final Integer PLAYERGAME_ID_EXISTENT = 1;
    private final Integer PLAYERGAME_ID_INEXISTENT = Integer.MAX_VALUE;

    /* stubs - begin */
    private Optional<PlayerGame> getEntityStubData() {
        PlayerGame playerGame = new PlayerGame();
        playerGame.setId(PLAYERGAME_ID_EXISTENT);

        return Optional.of(playerGame);
    }
    /* stubs - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenPlayerGameIsExistent_ReturnAnswer() {
        Optional<PlayerGame> entity = getEntityStubData();

        when(repository.findById(PLAYERGAME_ID_EXISTENT)).thenReturn(entity);

        Optional<PlayerGame> entityReturned = service.findById(PLAYERGAME_ID_EXISTENT);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(PLAYERGAME_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(PLAYERGAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenPlayerGameIsInexistent_ReturnNull() {
        when(repository.findById(PLAYERGAME_ID_INEXISTENT)).thenReturn(null);

        Optional<PlayerGame> entityReturned = service.findById(PLAYERGAME_ID_INEXISTENT);

        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(PLAYERGAME_ID_INEXISTENT);
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
        Assertions.assertEquals(PLAYERGAME_ID_EXISTENT.intValue(), entityReturned.getId(), "failure - expected id attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* removeCreditsFromPlayer - end */
}
