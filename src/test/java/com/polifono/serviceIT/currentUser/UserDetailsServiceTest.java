package com.polifono.serviceIT.currentUser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.domain.CurrentUser;
import com.polifono.service.currentUser.UserDetailsServiceImpl;
import com.polifono.service.impl.PlayerServiceImpl;

/**
 * Unit test methods for the UserDetailsServiceImpl.
 * 
 */
@Transactional
public class UserDetailsServiceTest {

	private UserDetailsServiceImpl service = new UserDetailsServiceImpl(new PlayerServiceImpl());
	
	private final String PLAYER_EMAIL_EXISTENT = "flavio10araujo@yahoo.com.br";
	
	@Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* loadUserByUsername - begin */
    @Test
    @Ignore
    public void loadUserByUsername() {
    	
    	CurrentUser currentUser = service.loadUserByUsername(PLAYER_EMAIL_EXISTENT);
    	
    	Assert.assertNotNull(currentUser.getUser());
    	Assert.assertTrue("failure - expected id user bigger than zero", currentUser.getId() > 0);
    	Assert.assertTrue("failure - expected role user [ADMIN, USER or TEACHER]", 
    			(currentUser.getRole().toString().equals("ADMIN")) ||
    			(currentUser.getRole().toString().equals("USER")) ||
    			(currentUser.getRole().toString().equals("TEACHER"))
    		);
    }
    /* loadUserByUsername - end */
}