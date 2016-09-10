package com.polifono.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;

/**
 * Unit test methods for the AnswerService.
 * 
 */
@Transactional
public class AnswerServiceTest extends AbstractTest {
	
	@Autowired
    private IAnswerService service;

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
    
    /*@Test
    public void findAll_ListIsNull_ExceptionThrown() {
    	
    }*/
    
    //findAnswersByGame
    
    //findAnswersByGameAndLevel
    
    //findAnswersByMap
    
    //findAnswersByPhase
    
    //findAnswersByQuestion
}