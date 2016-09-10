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
	
	private int gameId = 1;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    @Test
    public void findAll_ListIsNull_ExceptionThrown() {
    	Assert.assertNotNull("failure - expected not null", service.findAll());
    }
    
    @Test
    public void findAll_ListHasSizeZero_ExceptionThrown() {
    	List<Level> list = service.findAll();
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }

    @Test
    public void findByGame_ListIsNullForValidGame_ExceptionThrown() {
    	Assert.assertNotNull("failure - not expected list size 0", service.findByGame(gameId));
    }
    
    @Test
    public void findByGame_ListHasSizeZeroForValidGame_ExceptionThrown() {
    	List<Level> list = service.findByGame(gameId);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    
    @Test
    public void findByGame_ListHasValuesForInvalidGame_ExceptionThrown() {
    	List<Level> list = service.findByGame(0);
    	Assert.assertEquals("failure - expected list size 0", 0, list.size());
    	list = service.findByGame(-1);
    	Assert.assertEquals("failure - expected list size 0", 0, list.size());
    }
}