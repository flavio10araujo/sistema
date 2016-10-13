package com.polifono.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.polifono.AbstractControllerTest;
import com.polifono.domain.Game;
import com.polifono.service.IGameService;

/**
 * Unit tests for the GameController using Spring MVC Mocks.
 * 
 */
@Transactional
public class GameControllerTest extends AbstractControllerTest {
	
	@Mock
	private IGameService gameService;
	
	@InjectMocks
	private GameController controller;

	@Before
	public void setUp() {
		// Initialize Mockito annotated components
        MockitoAnnotations.initMocks(this);
        // Prepare the Spring MVC Mock components for standalone testing
        super.setUp(controller);
		//super.setUp();
	}
	
	private List<Game> getEntityListStubData() {
        List<Game> list = new ArrayList<Game>();
        
        Game entity1 = getEntityStubData();
        entity1.setName(entity1.getName() + "1");
        entity1.setNamelink(entity1.getNamelink() + "1");
        
        Game entity2 = getEntityStubData();
        entity2.setId(entity2.getId() + 1);
        entity2.setOrder(entity2.getOrder() + 1);
        entity2.setName(entity2.getName() + "2");
        entity2.setNamelink(entity2.getNamelink() + "2");
        
        list.add(entity1);
        list.add(entity2);
        
        return list;
    }

    private Game getEntityStubData() {
        Game entity = new Game();
        entity.setId(123);
        entity.setName("Game name");
        entity.setNamelink("gamename");
        entity.setOrder(3);
        return entity;
    }
	
	/* listGames - begin */
	@Test
	public void listGames() throws Exception {
		// Create some test data.
        List<Game> list = getEntityListStubData();

        // Stub the GameService.findAll method return value.
        when(gameService.findAll()).thenReturn(list);

        // Perform the behavior being tested.
        String uri = "/games";

        /*
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        // Extract the response status and body.
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        // Verify the GameService.findAll method was invoked once.
        verify(gameService, times(1)).findAll();
        
        // Perform standard JUnit assertions on the response.
        Assert.assertEquals("failure - expected HTTP status 200", HTTP_STATUS_OK, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
        */
        
        mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("games/index"))
                .andExpect(forwardedUrl("games/index"))
                .andExpect(model().attribute("games", hasSize(2)))
                .andExpect(model().attribute("games", hasItem(
                        allOf(
                                hasProperty("id", is(123)),
                                hasProperty("name", is("Game name1")),
                                hasProperty("namelink", is("gamename1"))
                        )
                )))
                .andExpect(model().attribute("games", hasItem(
                        allOf(
                                hasProperty("id", is(124)),
                                hasProperty("name", is("Game name2")),
                                hasProperty("namelink", is("gamename2"))
                        )
                )));
 
        verify(gameService, times(1)).findAll();
        verifyNoMoreInteractions(gameService);
	}
	/* listGames - end */
	
	/* listLevelsOfTheGame - begin */
	@Test
	public void listLevelsOfTheGame_WhenTheGameDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = "game_inexistent";
		
		when(gameService.findByNamelink(gameName)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void listLevelsOfTheGame_WhenThePlayerHasNeverPlayedAnyPhaseOfTheGame_ListLevels() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = "recorder";
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		//when(controller.currentAuthenticatedUser().getUser().getId()).thenReturn(1);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
				.andExpect(status().isOk())
				.andReturn();
		
		int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_OK, status);
	}
	
	/*@Test
	public void listLevelsOfTheGame_WhenThePlayerHasalreadyPlayedAtLeastOnePhaseOfTheGame_ListLevels() throws Exception {
		
	}*/
	/* listLevelsOfTheGame - end */
	
	//listPhasesOfTheMap
	
	//initPhase
	
	//initTest
	
	//showResultTest
}