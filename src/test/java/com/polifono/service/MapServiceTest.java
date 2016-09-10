package com.polifono.service;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;

/**
 * Unit test methods for the MapService.
 * 
 */
@Transactional
public class MapServiceTest extends AbstractTest {

	@Autowired
    private IMapService service;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    //save
    
    //delete
    
    //find
    
    //findAll
    
    //findMapsByGame
    
    //findMapsByGameAndLevel
    
    //findMapByGameAndLevel
    
    //findMapsByGameLevelAndOrder
    
    //findNextMapSameLevel

}