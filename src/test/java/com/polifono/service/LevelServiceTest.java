package com.polifono.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Level;

/**
 * Unit test methods for the LevelService.
 * 
 */
@Transactional
public class LevelServiceTest extends AbstractTest {

	@Autowired
    private ILevelService service;
	
	private final Integer GAME_ID_EXISTENT = 1;

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
    public void findAll_WhenListAllLevels_ReturnList() {
    	List<Level> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findByGame - begin */
    @Test
    public void findByGame_WhenSearchByGameExistent_ReturnLevel() {
    	List<Level> list = service.findByGame(GAME_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    
    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
    	List<Level> list = service.findByGame(0);
    	Assert.assertEquals("failure - expected list size 0", 0, list.size());
    	list = service.findByGame(-1);
    	Assert.assertEquals("failure - expected list size 0", 0, list.size());
    }
    /* findByGame - end */
}