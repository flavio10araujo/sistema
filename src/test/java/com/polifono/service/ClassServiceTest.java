package com.polifono.service;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;

/**
 * Unit test methods for the ClassService.
 * 
 */
@Transactional
public class ClassServiceTest extends AbstractTest {

	@Autowired
    private IClassService service;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    //create
    
    //save
    
    //delete
    
    //find
    
    //findAll
    
    //findByTeacherAndStatus
}