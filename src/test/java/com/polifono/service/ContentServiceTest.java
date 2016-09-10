package com.polifono.service;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;

/**
 * Unit test methods for the ContentService.
 * 
 */
@Transactional
public class ContentServiceTest extends AbstractTest {

	@Autowired
    private IContentService service;

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
    
    //findAllText
    
    //findContentsTextByGame
    
    //findContentsTextByGameAndLevel
    
    //findContentsTextByMap
    
    //findContentsTextByPhase
    
    //findAllTest
    
    //findContentsTestByGame
    
    //findContentsTestByGameAndLevel
    
    //findContentsTestByMap
    
    //findContentsTestByPhase
    
    //findContentByPhaseAndOrder
}