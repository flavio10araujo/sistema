package com.polifono.serviceIT.currentUser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.CurrentUser;
import com.polifono.domain.Player;
import com.polifono.service.IPlayerService;
import com.polifono.service.currentUser.ICurrentUserService;

/**
 * Unit test methods for the CurrentUserServiceImpl.
 * 
 */
@Transactional
public class CurrentUserServiceTest extends AbstractTest {

	@Autowired
	private ICurrentUserService service;
	
	@Autowired
    private IPlayerService playerService;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	
	@Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* canAccessUser - begin */
    @Test
    public void canAccessUser_WhenUserHasAccess_returnTrue() {
    	Player player = playerService.findOne(PLAYER_ID_EXISTENT);
    	CurrentUser currentUser = new CurrentUser(player);
    	Assert.assertTrue("failure - expected true", service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }
    /* canAccessUser - end */
}