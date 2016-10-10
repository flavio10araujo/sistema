package com.polifono.service.currentUser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.CurrentUser;
import com.polifono.domain.Player;
import com.polifono.domain.Role;

/**
 * Unit test methods for the CurrentUserServiceImpl.
 * 
 */
@Transactional
public class CurrentUserServiceTest extends AbstractTest {

	@Autowired
	private ICurrentUserService service;
	
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
    public void canAccessUser_WhenCurrentUserIsNull_returnFalse() {
    	CurrentUser currentUser = null;
    	Assert.assertFalse(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }
    
    @Test
    public void canAccessUser_WhenRoleIsAdmin_returnTrue() {
    	Player player = new Player();
    	player.setRole(Role.ADMIN);
    	CurrentUser currentUser = new CurrentUser(player);
    	Assert.assertTrue(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }
    
    @Test
    public void canAccessUser_WhenCurrentUserIdIsEqualsUserId_returnTrue() {
    	Player player = new Player();
    	player.setRole(Role.ADMIN);
    	CurrentUser currentUser = new CurrentUser(player);
    	Assert.assertTrue(service.canAccessUser(currentUser, PLAYER_ID_EXISTENT.longValue()));
    }
    /* canAccessUser - end */
}