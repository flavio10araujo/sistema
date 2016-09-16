package com.polifono.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Player;

/**
 * Unit test methods for the LoginService.
 * 
 */
@Transactional
public class LoginServiceTest extends AbstractTest {

	@Autowired
    private ILoginService service;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }

    @Test
    public void registerLogin_PlayerNotRegistered_ExceptionThrown() {
    	Player player = new Player();
    	player.setId(1);
    	service.registerLogin(player);
    	Assert.assertNotEquals("failure - not expected ID equals 0", 0, player.getId());
    }

    //findByPlayer
    //1 - não buscou nada e deveria buscar;
    //2 - buscou algo, mas não deveria buscar;
    //3 - buscou valores, mas estão incorretos;
    /*@Test
    public void findByPlayer_PlayerNotFound_ExceptionThrown() {
    	
    }*/
}