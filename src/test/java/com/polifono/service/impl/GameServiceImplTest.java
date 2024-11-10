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

import com.polifono.model.entity.Game;
import com.polifono.repository.IGameRepository;

/**
 * Unit test methods for the GameService.
 */
@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl service;

    @Mock
    private IGameRepository repository;

    private final String NAME_LINK = "recorder";

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllGames_ReturnList() {
        List<Game> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Game> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findByNamelink - begin */
    @Test
    public void findByNamelink_WhenSearchByNamelinkExistent_ReturnGame() {
        Optional<Game> entity = getEntityStubData();

        when(repository.findByNamelink(NAME_LINK)).thenReturn(entity.get());

        Optional<Game> entityReturned = service.findByNamelink(NAME_LINK);
        Assertions.assertTrue(entityReturned.isPresent(), "failure - expected not null for '" + NAME_LINK + "'");

        verify(repository, times(1)).findByNamelink(NAME_LINK);
        verifyNoMoreInteractions(repository);
    }
    /* findByNamelink - end */

    /* calculateScore - begin */
    @Test
    public void calculateScore_WhenFirstAttempt_ScoreReturnedIsTheSameThatTheGrade() {
        Assertions.assertEquals(100, service.calculateScore(1, 100));
        Assertions.assertEquals(90, service.calculateScore(1, 90));
        Assertions.assertEquals(80, service.calculateScore(1, 80));
        Assertions.assertEquals(70, service.calculateScore(1, 70));
    }

    @Test
    public void calculateScore_WhenSecondAttempt_ReturnScoreWithDiscount() {
        Assertions.assertEquals(70, service.calculateScore(2, 100));
        Assertions.assertEquals(65, service.calculateScore(2, 90));
        Assertions.assertEquals(60, service.calculateScore(2, 80));
        Assertions.assertEquals(50, service.calculateScore(2, 70));
    }

    @Test
    public void calculateScore_WhenThirdAttempt_ReturnScoreWithDiscount() {
        Assertions.assertEquals(50, service.calculateScore(3, 100));
        Assertions.assertEquals(45, service.calculateScore(3, 90));
        Assertions.assertEquals(40, service.calculateScore(3, 80));
        Assertions.assertEquals(30, service.calculateScore(3, 70));
    }

    @Test
    public void calculateScore_WhenMoreThanThreeAttempts_ReturnScoreAlways10() {
        Assertions.assertEquals(10, service.calculateScore(4, 100));
        Assertions.assertEquals(10, service.calculateScore(4, 90));
        Assertions.assertEquals(10, service.calculateScore(4, 80));
        Assertions.assertEquals(10, service.calculateScore(4, 70));
        Assertions.assertEquals(10, service.calculateScore(5, 100));
        Assertions.assertEquals(10, service.calculateScore(5, 90));
        Assertions.assertEquals(10, service.calculateScore(5, 80));
        Assertions.assertEquals(10, service.calculateScore(5, 70));
    }
    /* calculateScore - end */

    /* calculateGrade - begin */
    /* calculateGrade - end */

    /* getPhaseOfTheTest - begin */
    /* getPhaseOfTheTest - end */

    /* stubs - begin */
    public Optional<Game> getEntityStubData() {
        Game game = new Game();
        return Optional.of(game);
    }

    private List<Game> getEntityListStubData() {
        List<Game> list = new ArrayList<>();

        Game entity1 = getEntityStubData().get();
        Game entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
