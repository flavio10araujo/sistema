package com.polifono.serviceIT;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.service.IGameService;

/**
 * Unit test methods for the GameService.
 * 
 */
@Transactional
public class GameServiceTest extends AbstractTest {

	@Autowired
    private IGameService service;
	
	private final String NAME_LINK = "recorder";

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }

    /* findAll - begin */
    @Test
    public void findAll_ListIsNull_ExceptionThrown() {
    	List<Game> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    }
    
    @Test
    public void findAll_ListHasSizeZero_ExceptionThrown() {
    	List<Game> list = service.findAll();
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */

    /* findByNamelink - begin */
    @Test
    public void findByNamelink_ReturnIsNull_ExceptionThrown() {
    	Game item = service.findByNamelink(NAME_LINK);
    	Assert.assertNotNull("failure - expected not null for '" + NAME_LINK + "'", item);
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
    public void calculateScore_WhenMoreAttempts_ReturnScoreAlways10() {
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
}