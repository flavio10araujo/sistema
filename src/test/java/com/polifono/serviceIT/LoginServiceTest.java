package com.polifono.serviceIT;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Login;
import com.polifono.domain.Player;
import com.polifono.service.ILoginService;

/**
 * Unit test methods for the LoginService.
 * 
 */
@Transactional
public class LoginServiceTest extends AbstractTest {

	@Autowired
    private ILoginService service;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	private final Integer PLAYER_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }

    /* registerLogin - begin */
    @Test
    public void registerLogin() {
    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);
    	Login entity = service.registerLogin(player);
    	Assert.assertNotEquals("failure - not expected ID equals 0", 0, entity.getId());
    }
    /* registerLogin - end */

    /* findByPlayer - begin */
    @Test
    public void findByPlayer_PlayerExistent_returnList() {
    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);
    	service.registerLogin(player);

    	List<Login> list = service.findByPlayer(PLAYER_ID_EXISTENT);
    	Assert.assertNotNull("failure - not expected null", list);
    	Assert.assertNotEquals("failure - not size 0", 0, list.size());
    }
    
    @Test
    public void findByPlayer_PlayerInexistent_returnNull() {
    	List<Login> list = service.findByPlayer(PLAYER_ID_INEXISTENT);
    	Assert.assertNull("failure - expected null", list);
    }
    /* findByPlayer - end */
}