package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.repository.IGameRepository;
import com.polifono.service.impl.GameServiceImpl;

/**
 * Unit test methods for the GameService.
 */
public class GameServiceTest extends AbstractTest {

    @Autowired
    private IGameService service;

    @Mock
    private IGameRepository repository;

    private final String NAME_LINK = "recorder";

    @BeforeEach
    public void setUp() {
        // Do something before each test method.
        MockitoAnnotations.initMocks(this);
        service = new GameServiceImpl(repository);
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test method.
    }

    /* stubs - begin */
    public Optional<Game> getEntityStubData() {
        Game game = new Game();
        return Optional.of(game);
    }

    private List<Game> getEntityListStubData() {
        List<Game> list = new ArrayList<Game>();

        Game entity1 = getEntityStubData().get();
        Game entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */

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

        Game entityReturned = service.findByNamelink(NAME_LINK);
        Assertions.assertNotNull(entityReturned, "failure - expected not null for '" + NAME_LINK + "'");

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
}
