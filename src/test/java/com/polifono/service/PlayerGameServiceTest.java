package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;
import com.polifono.service.impl.PlayerGameServiceImpl;

/**
 * Unit test methods for the PlayerGameService.
 * 
 */
public class PlayerGameServiceTest extends AbstractTest {

    private IPlayerGameService service;
    
    @Mock
    private IPlayerGameRepository repository;
	
	private final Integer PLAYERGAME_ID_EXISTENT = 1;
	private final Integer PLAYERGAME_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new PlayerGameServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private PlayerGame getEntityStubData() {
    	PlayerGame playerGame = new PlayerGame();
    	playerGame.setId(PLAYERGAME_ID_EXISTENT);    	
    	
    	return playerGame;
    }
    /* stubs - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenPlayerGameIsExistent_ReturnAnswer() {
    	PlayerGame entity = getEntityStubData();
    	
    	when(repository.findOne(PLAYERGAME_ID_EXISTENT)).thenReturn(entity);
    	
    	PlayerGame entityReturned = service.findOne(PLAYERGAME_ID_EXISTENT);
        
    	Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", PLAYERGAME_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(PLAYERGAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenPlayerGameIsInexistent_ReturnNull() {
    	when(repository.findOne(PLAYERGAME_ID_INEXISTENT)).thenReturn(null);
    	
    	PlayerGame entityReturned = service.findOne(PLAYERGAME_ID_INEXISTENT);
    	
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findOne(PLAYERGAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* removeCreditsFromPlayer - begin */
    @Test
    public void removeCreditsFromPlayer_WhenRemoveOneCredit_ReturnPlayerWithOneCreditLess() {
    	PlayerGame entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	PlayerGame entityReturned = service.removeCreditsFromPlayer(entity, 1);
    	
    	Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", PLAYERGAME_ID_EXISTENT.intValue(), entityReturned.getId());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* removeCreditsFromPlayer - end */
}