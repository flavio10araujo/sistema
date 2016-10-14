package com.polifono.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.polifono.AbstractControllerTest;
import com.polifono.domain.CurrentUser;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Role;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;

/**
 * Unit tests for the GameController using Spring MVC Mocks.
 * 
 */
@Transactional
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityContextHolder.class})
public class GameControllerTest extends AbstractControllerTest {
	
	@Mock
	private IGameService gameService;
	
	@Mock
	private IPlayerPhaseService playerPhaseService;
	
	@Mock
	private ILevelService levelService;
	
	@Mock
	private IPhaseService phaseService;
	
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
		
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

		Player player = new Player();
		player.setId(25);
		player.setEmail("logged-in@test.com");
		player.setPassword("password1");
		player.setRole(Role.USER);
		CurrentUser currentUser = new CurrentUser(player);
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
        
        when(playerPhaseService.findLastPhaseCompleted(25, 123)).thenReturn(null);
        
        List<Level> levels = new ArrayList<Level>();
        Level l1 = new Level();
        Level l2 = new Level();
        Level l3 = new Level();
        Level l4 = new Level();
        Level l5 = new Level();
        l1.setOrder(1);l1.setOpened(true);
        l2.setOrder(2);
        l3.setOrder(3);
        l4.setOrder(4);
        l5.setOrder(5);
        levels.add(l1);
        levels.add(l2);
        levels.add(l3);
        levels.add(l4);
        levels.add(l5);
        when(levelService.flagLevelsToOpenedOrNot(123, 1)).thenReturn(levels);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
	        .andExpect(status().isOk())
	        .andExpect(view().name("games/level"))
	        .andExpect(forwardedUrl("games/level"))
	        .andExpect(model().attribute("game", hasProperty("id", is(123))))
	        .andExpect(model().attribute("levels", hasSize(5)))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(1)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(2)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(3)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(4)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(5)),
	                        hasProperty("opened", is(false))
	                )
	        )));
	}
	
	@Test
	public void listLevelsOfTheGame_WhenThePlayerHasAlreadyPlayedAtLeastOnePhaseOfTheGame_ListLevels() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = "recorder";
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

		Player player = new Player();
		player.setId(25);
		player.setEmail("logged-in@test.com");
		player.setPassword("password1");
		player.setRole(Role.USER);
		CurrentUser currentUser = new CurrentUser(player);
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
        
        PlayerPhase lastPlayerPhaseCompleted = new PlayerPhase();
        Level level = new Level();
        level.setId(1);
        level.setOrder(1);
        Map map = new Map();
        map.setLevel(level);
        Phase phase = new Phase();
        phase.setMap(map);
        phase.setId(1);
        lastPlayerPhaseCompleted.setPhase(phase);
        when(playerPhaseService.findLastPhaseCompleted(25, 123)).thenReturn(lastPlayerPhaseCompleted);
        
        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(30);
        when(phaseService.findLastPhaseOfTheLevel(123, 1)).thenReturn(lastPhaseOfTheLevel);
        
        List<Level> levels = new ArrayList<Level>();
        Level l1 = new Level();
        Level l2 = new Level();
        Level l3 = new Level();
        Level l4 = new Level();
        Level l5 = new Level();
        l1.setOrder(1);l1.setOpened(true);
        l2.setOrder(2);l2.setOpened(true);
        l3.setOrder(3);
        l4.setOrder(4);
        l5.setOrder(5);
        levels.add(l1);
        levels.add(l2);
        levels.add(l3);
        levels.add(l4);
        levels.add(l5);
        when(levelService.flagLevelsToOpenedOrNot(123, 1)).thenReturn(levels);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
	        .andExpect(status().isOk())
	        .andExpect(view().name("games/level"))
	        .andExpect(forwardedUrl("games/level"))
	        .andExpect(model().attribute("game", hasProperty("id", is(123))))
	        .andExpect(model().attribute("levels", hasSize(5)))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(1)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(2)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(3)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(4)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(5)),
	                        hasProperty("opened", is(false))
	                )
	        )));
	}
	
	@Test
	public void listLevelsOfTheGame_WhenThePlayerHasPlayedTheLastPhaseOfTheALevel_ListLevels() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = "recorder";
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

		Player player = new Player();
		player.setId(25);
		player.setEmail("logged-in@test.com");
		player.setPassword("password1");
		player.setRole(Role.USER);
		CurrentUser currentUser = new CurrentUser(player);
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
        
        PlayerPhase lastPlayerPhaseCompleted = new PlayerPhase();
        Level level = new Level();
        level.setId(1);
        Map map = new Map();
        map.setLevel(level);
        Phase phase = new Phase();
        phase.setMap(map);
        phase.setId(30);
        lastPlayerPhaseCompleted.setPhase(phase);
        when(playerPhaseService.findLastPhaseCompleted(25, 123)).thenReturn(lastPlayerPhaseCompleted);
        
        Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(30);
        when(phaseService.findLastPhaseOfTheLevel(123, 1)).thenReturn(lastPhaseOfTheLevel);
        
        List<Level> levels = new ArrayList<Level>();
        Level l1 = new Level();
        Level l2 = new Level();
        Level l3 = new Level();
        Level l4 = new Level();
        Level l5 = new Level();
        l1.setOrder(1);l1.setOpened(true);
        l2.setOrder(2);l2.setOpened(true);
        l3.setOrder(3);
        l4.setOrder(4);
        l5.setOrder(5);
        levels.add(l1);
        levels.add(l2);
        levels.add(l3);
        levels.add(l4);
        levels.add(l5);
        when(levelService.flagLevelsToOpenedOrNot(123, 1)).thenReturn(levels);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
	        .andExpect(status().isOk())
	        .andExpect(view().name("games/level"))
	        .andExpect(forwardedUrl("games/level"))
	        .andExpect(model().attribute("game", hasProperty("id", is(123))))
	        .andExpect(model().attribute("levels", hasSize(5)))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(1)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(2)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(3)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(4)),
	                        hasProperty("opened", is(false))
	                )
	        )))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(5)),
	                        hasProperty("opened", is(false))
	                )
	        )));
	}
	/* listLevelsOfTheGame - end */
	
	//listPhasesOfTheMap
	
	//initPhase
	
	//initTest
	
	//showResultTest
}