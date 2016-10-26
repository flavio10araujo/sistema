package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.repository.IGameRepository;
import com.polifono.service.impl.GameServiceImpl;

/**
 * Unit test methods for the GameService.
 * 
 */
public class GameServiceTest extends AbstractTest {

	@Autowired
    private IGameService service;
	
	@Mock
	private IGameRepository repository;
	
	private final String NAME_LINK = "recorder";

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new GameServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    public Game getEntityStubData() {
    	Game game = new Game();
    	return game;
    }
    
    private List<Game> getEntityListStubData() {
    	List<Game> list = new ArrayList<Game>();
    	
    	Game entity1 = getEntityStubData();
    	Game entity2 = getEntityStubData();
    	
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
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
    	verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findByNamelink - begin */
    @Test
    public void findByNamelink_WhenSearchByNamelinkExistent_ReturnGame() {
    	Game entity = getEntityStubData();
    	
    	when(repository.findByNamelink(NAME_LINK)).thenReturn(entity);
    	
    	Game entityReturned = service.findByNamelink(NAME_LINK);
    	Assert.assertNotNull("failure - expected not null for '" + NAME_LINK + "'", entityReturned);
    	
    	verify(repository, times(1)).findByNamelink(NAME_LINK);
    	verifyNoMoreInteractions(repository);
    }
    /* findByNamelink - end */
    
    /* calculateScore - begin */
    @Test
    public void calculateScore_WhenFirstAttempt_ScoreReturnedIsTheSameThatTheGrade() {
    	Assert.assertEquals(100, service.calculateScore(1, 100));
    	Assert.assertEquals(90, service.calculateScore(1, 90));
    	Assert.assertEquals(80, service.calculateScore(1, 80));
    	Assert.assertEquals(70, service.calculateScore(1, 70));
    }
    
    @Test
    public void calculateScore_WhenSecondAttempt_ReturnScoreWithDiscount() {
    	Assert.assertEquals(70, service.calculateScore(2, 100));
    	Assert.assertEquals(65, service.calculateScore(2, 90));
    	Assert.assertEquals(60, service.calculateScore(2, 80));
    	Assert.assertEquals(50, service.calculateScore(2, 70));
    }
    
    @Test
    public void calculateScore_WhenThirdAttempt_ReturnScoreWithDiscount() {
    	Assert.assertEquals(50, service.calculateScore(3, 100));
    	Assert.assertEquals(45, service.calculateScore(3, 90));
    	Assert.assertEquals(40, service.calculateScore(3, 80));
    	Assert.assertEquals(30, service.calculateScore(3, 70));
    }
    
    @Test
    public void calculateScore_WhenMoreThanThreeAttempts_ReturnScoreAlways10() {
    	Assert.assertEquals(10, service.calculateScore(4, 100));
    	Assert.assertEquals(10, service.calculateScore(4, 90));
    	Assert.assertEquals(10, service.calculateScore(4, 80));
    	Assert.assertEquals(10, service.calculateScore(4, 70));
    	Assert.assertEquals(10, service.calculateScore(5, 100));
    	Assert.assertEquals(10, service.calculateScore(5, 90));
    	Assert.assertEquals(10, service.calculateScore(5, 80));
    	Assert.assertEquals(10, service.calculateScore(5, 70));
    }
    /* calculateScore - end */
    
    /* calculateGrade - begin */
    /* calculateGrade - end */
    
    /* getPhaseOfTheTest - begin */
    /* getPhaseOfTheTest - end */
}