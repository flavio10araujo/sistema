package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.polifono.AbstractTest;
import com.polifono.domain.Level;
import com.polifono.repository.ILevelRepository;
import com.polifono.service.impl.LevelServiceImpl;

/**
 * Unit test methods for the LevelService.
 * 
 */
public class LevelServiceTest extends AbstractTest {

	@Autowired
    private ILevelService service;
	
	@Mock
	private ILevelRepository repository;
	
	private final Integer GAME_ID_EXISTENT = 1;
	private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new LevelServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Level getEntityStubData() {
    	Level level = new Level();
    	return level;
    }
    
    private List<Level> getEntityListStubData() {
    	List<Level> list = new ArrayList<Level>();
    	
    	Level entity1 = getEntityStubData();
    	Level entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    } 
    /* stubs - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllLevels_ReturnList() {
    	List<Level> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<Level> listReturned = service.findAll();
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */
    
    /* findByGame - begin */
    @Test
    public void findByGame_WhenSearchByGameExistent_ReturnLevel() {
    	List<Level> list = getEntityListStubData();
    	
    	when(repository.findByGame(GAME_ID_EXISTENT)).thenReturn(list);
    	
    	List<Level> listReturned = service.findByGame(GAME_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
    	when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<Level>());
    	
    	List<Level> listReturned = service.findByGame(GAME_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGame(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGame - end */
    
    /* flagLevelsToOpenedOrNot - begin */
    /* flagLevelsToOpenedOrNot - end */
}