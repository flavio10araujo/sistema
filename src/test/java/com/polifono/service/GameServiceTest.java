package com.polifono.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;

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
}