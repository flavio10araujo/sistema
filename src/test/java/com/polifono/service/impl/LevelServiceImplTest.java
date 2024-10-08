package com.polifono.service.impl;

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

import com.polifono.domain.Level;
import com.polifono.repository.ILevelRepository;

/**
 * Unit test methods for the LevelService.
 */
@ExtendWith(MockitoExtension.class)
public class LevelServiceImplTest {

    @InjectMocks
    private LevelServiceImpl service;

    @Mock
    private ILevelRepository repository;

    private final Integer GAME_ID_EXISTENT = 1;
    private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    /* stubs - begin */
    private Optional<Level> getEntityStubData() {
        Level level = new Level();
        return Optional.of(level);
    }

    private List<Level> getEntityListStubData() {
        List<Level> list = new ArrayList<>();

        Level entity1 = getEntityStubData().get();
        Level entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllLevels_ReturnList() {
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
    public void findByGame_WhenSearchByGameExistent_ReturnLevel() {
        List<Level> list = getEntityListStubData();

        when(repository.findByGame(GAME_ID_EXISTENT)).thenReturn(list);

        List<Level> listReturned = service.findByGame(GAME_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
        when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<Level>());

        List<Level> listReturned = service.findByGame(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected list size 0");

        verify(repository, times(1)).findByGame(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGame - end */

    /* flagLevelsToOpenedOrNot - begin */
    /* flagLevelsToOpenedOrNot - end */
}
