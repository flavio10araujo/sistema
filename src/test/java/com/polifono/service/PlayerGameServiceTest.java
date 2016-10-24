package com.polifono.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.PlayerGame;

import org.junit.Assert;

/**
 * Unit test methods for the PlayerGameService.
 * 
 */
@Transactional
public class PlayerGameServiceTest extends AbstractTest {

	@Autowired
    private IPlayerGameService service;
	
	private final Integer PLAYERGAME_ID_EXISTENT = 1;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenRemoveOneCredit_ReturnPlayerWithOneCreditLess() {
    	PlayerGame playerGame = service.findOne(PLAYERGAME_ID_EXISTENT);
    	int creditsOld = playerGame.getCredit();
    	service.removeCreditsFromPlayer(playerGame, 1);
    	PlayerGame entity = service.findOne(PLAYERGAME_ID_EXISTENT);
    	Assert.assertEquals("failure - expected attribute credits match", creditsOld - 1, entity.getCredit());
    }
    /* removeCreditsFromPlayer - end */
}