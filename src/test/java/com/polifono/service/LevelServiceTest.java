package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.entity.Level;
import com.polifono.repository.ILevelRepository;

/**
 * Unit test methods for the LevelService.
 */
@ExtendWith(MockitoExtension.class)
public class LevelServiceTest {

    @InjectMocks
    private LevelService service;

    @Mock
    private ILevelRepository repository;

    private final Integer GAME_ID_EXISTENT = 1;
    private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    /* findAll - begin */
    @Test
    public void whenFindAll_thenReturnList() {
        List<Level> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Level> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findByGame - begin */
    @Test
    public void givenGameExists_whenFindByGame_thenReturnLevel() {
        List<Level> list = getEntityListStubData();

        when(repository.findByGame(GAME_ID_EXISTENT)).thenReturn(list);

        List<Level> listReturned = service.findByGame(GAME_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenGameDoesNotExist_whenFindByGame_thenReturnEmptyList() {
        when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Level> listReturned = service.findByGame(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected list size 0");

        verify(repository, times(1)).findByGame(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGame - end */

    /* flagLevelsToOpenedOrNot - begin */
    @Test
    public void givenLevels_whenFlagLevelsToOpenedOrNot_thenReturnLevelsWithCorrectOpenedFlag() {
        int gameId = GAME_ID_EXISTENT;
        int levelPermitted = 2;

        List<Level> levels = getEntityListStubData();
        levels.get(0).setOrder(1);
        levels.get(1).setOrder(3);

        when(repository.findByGame(gameId)).thenReturn(levels);

        List<Level> flaggedLevels = service.flagLevelsToOpenedOrNot(gameId, levelPermitted);

        Assertions.assertNotNull(flaggedLevels, "failure - expected not null");
        Assertions.assertEquals(2, flaggedLevels.size(), "failure - expected size 2");
        Assertions.assertTrue(flaggedLevels.get(0).isOpened(), "failure - expected level 1 to be opened");
        Assertions.assertFalse(flaggedLevels.get(1).isOpened(), "failure - expected level 3 to be closed");

        verify(repository, times(1)).findByGame(gameId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenNoLevels_whenFlagLevelsToOpenedOrNot_thenReturnEmptyList() {
        int gameId = GAME_ID_EXISTENT;
        int levelPermitted = 2;

        when(repository.findByGame(gameId)).thenReturn(new ArrayList<>());

        List<Level> flaggedLevels = service.flagLevelsToOpenedOrNot(gameId, levelPermitted);

        Assertions.assertNotNull(flaggedLevels, "failure - expected not null");
        Assertions.assertEquals(0, flaggedLevels.size(), "failure - expected size 0");

        verify(repository, times(1)).findByGame(gameId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenLevelsWithAllOpened_whenFlagLevelsToOpenedOrNot_thenReturnAllOpenedLevels() {
        int gameId = GAME_ID_EXISTENT;
        int levelPermitted = 3;

        List<Level> levels = getEntityListStubData();
        levels.get(0).setOrder(1);
        levels.get(1).setOrder(2);

        when(repository.findByGame(gameId)).thenReturn(levels);

        List<Level> flaggedLevels = service.flagLevelsToOpenedOrNot(gameId, levelPermitted);

        Assertions.assertNotNull(flaggedLevels, "failure - expected not null");
        Assertions.assertEquals(2, flaggedLevels.size(), "failure - expected size 2");
        Assertions.assertTrue(flaggedLevels.get(0).isOpened(), "failure - expected level 1 to be opened");
        Assertions.assertTrue(flaggedLevels.get(1).isOpened(), "failure - expected level 2 to be opened");

        verify(repository, times(1)).findByGame(gameId);
        verifyNoMoreInteractions(repository);
    }
    /* flagLevelsToOpenedOrNot - end */

    /* stubs - begin */
    private Level getEntityStubData() {
        return new Level();
    }

    private List<Level> getEntityListStubData() {
        List<Level> list = new ArrayList<>();

        Level entity1 = getEntityStubData();
        Level entity2 = getEntityStubData();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
