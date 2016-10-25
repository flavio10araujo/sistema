package com.polifono.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.polifono.AbstractControllerTest;
import com.polifono.domain.Answer;
import com.polifono.domain.Content;
import com.polifono.domain.CurrentUser;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.domain.Question;
import com.polifono.domain.Role;
import com.polifono.service.IContentService;
import com.polifono.service.IGameService;
import com.polifono.service.ILevelService;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IQuestionService;

/**
 * Unit tests for the GameController using Spring MVC Mocks.
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityContextHolder.class}) // For emulating a logged in user.
public class GameControllerTest extends AbstractControllerTest {
	
	@Mock
	private IGameService gameService;
	
	@Mock
	private IPlayerPhaseService playerPhaseService;
	
	@Mock
	private ILevelService levelService;
	
	@Mock
	private IPhaseService phaseService;
	
	@Mock
	private IMapService mapService;
	
	@Mock
	private IContentService contentService;
	
	@Mock
	private IPlayerService playerService;
	
	@Mock
	private IQuestionService questionService;
	
	@InjectMocks
	private GameController controller; // All the Mock object are injected in the InjectMocks object.
	
	private final Integer GAME_ID_EXISTENT = 123;
	private final Integer GAME_ORDER_EXISTENT = 3;
	private final String GAME_NAME_EXISTENT = "recorder";
	private final Integer PLAYER_ID_EXISTENT = 25;
	private final Integer MAP_ID_EXISTENT = 1;

	@Before
	public void setUp() {
		// Initialize Mockito annotated components
        MockitoAnnotations.initMocks(this);
        // Prepare the Spring MVC Mock components for standalone testing
        super.setUp(controller);
	}
	
	/* stubs - begin */
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
        entity.setId(GAME_ID_EXISTENT);
        entity.setOrder(GAME_ORDER_EXISTENT);
        entity.setName("Game name");
        entity.setNamelink("gamename");
        return entity;
    }
    
    private CurrentUser getCurrentUserStubData() {
    	Player player = new Player();
		player.setId(PLAYER_ID_EXISTENT);
		player.setEmail("logged-in-user@test.com");
		player.setPassword("password1");
		player.setRole(Role.USER);
		return new CurrentUser(player);
    }
    
    private List<Level> getLevelListStubData() {
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
        return levels;
    }
    
    private PlayerPhase getLastPhaseCompletedStubData() {
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
        return lastPlayerPhaseCompleted;
    }
    
    private PlayerPhase getLastPhaseCompletedLastPhaseOfTheLevelStubData() {
    	PlayerPhase lastPlayerPhaseCompleted = getLastPhaseCompletedStubData();
        Phase phase = lastPlayerPhaseCompleted.getPhase();
        phase.setId(30);
        lastPlayerPhaseCompleted.setPhase(phase);
        return lastPlayerPhaseCompleted;
    }
    
    private Phase getLastPhaseOfTheLevelStubData() {
    	Phase lastPhaseOfTheLevel = new Phase();
        lastPhaseOfTheLevel.setId(30);
        return lastPhaseOfTheLevel;
    }

    private List<Phase> getPhasesCheckedByMapStubData() {
    	List<Phase> phases = new ArrayList<Phase>();
    	
    	Phase p1 = new Phase();
    	Phase p2 = new Phase();
    	Phase p3 = new Phase();
    	Phase p4 = new Phase();
    	Phase p5 = new Phase();
    	Phase p6 = new Phase();
    	Phase p7 = new Phase();
    	Phase p8 = new Phase();
    	Phase p9 = new Phase();
    	Phase p10 = new Phase();
    	Phase p11 = new Phase();
    	Phase p12 = new Phase();
    	Phase p13 = new Phase();
    	Phase p14 = new Phase();
    	Phase p15 = new Phase();
    	Phase p16 = new Phase();
    	Phase p17 = new Phase();
    	Phase p18 = new Phase();
    	Phase p19 = new Phase();
    	Phase p20 = new Phase();
    	Phase p21 = new Phase();
    	Phase p22 = new Phase();
    	Phase p23 = new Phase();
    	Phase p24 = new Phase();
    	Phase p25 = new Phase();
    	Phase p26 = new Phase();
    	Phase p27 = new Phase();
    	Phase p28 = new Phase();
    	Phase p29 = new Phase();
    	Phase p30 = new Phase();
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	p1.setMap(map);
    	p1.setId(1);
    	p1.setOpened(true);
    	
    	p2.setId(2);
    	p2.setOpened(true);
    	
    	phases.add(p1);
    	phases.add(p2);
    	phases.add(p3);
    	phases.add(p4);
    	phases.add(p5);
    	phases.add(p6);
    	phases.add(p7);
    	phases.add(p8);
    	phases.add(p9);
    	phases.add(p10);
    	phases.add(p11);
    	phases.add(p12);
    	phases.add(p13);
    	phases.add(p14);
    	phases.add(p15);
    	phases.add(p16);
    	phases.add(p17);
    	phases.add(p18);
    	phases.add(p19);
    	phases.add(p20);
    	phases.add(p21);
    	phases.add(p22);
    	phases.add(p23);
    	phases.add(p24);
    	phases.add(p25);
    	phases.add(p26);
    	phases.add(p27);
    	phases.add(p28);
    	phases.add(p29);
    	phases.add(p30);
    	
    	return phases;
    }
    
    private List<Question> getQuestionsByContentStubData() {
    	List<Question> questions = new ArrayList<Question>();
    	
    	List<Answer> answers = new ArrayList<Answer>();
    	Answer a1 = new Answer();
    	Answer a2 = new Answer();
    	Answer a3 = new Answer();
    	Answer a4 = new Answer();
    	answers.add(a1);
    	answers.add(a2);
    	answers.add(a3);
    	answers.add(a4);
    	
    	Question q1 = new Question();
    	q1.setId(101);
    	q1.setName("Question 01");
    	q1.setOrder(1);
    	q1.setAnswers(answers);
    	
    	Question q2 = new Question();
    	q2.setId(102);
    	q2.setName("Question 02");
    	q2.setOrder(2);
    	
    	Question q3 = new Question();
    	q3.setId(103);
    	q3.setName("Question 03");
    	q3.setOrder(3);
    	
    	Question q4 = new Question();
    	q4.setId(104);
    	q4.setName("Question 04");
    	q4.setOrder(4);
    	
    	Question q5 = new Question();
    	q5.setId(105);
    	q5.setName("Question 05");
    	q5.setOrder(5);
    	
    	questions.add(q1);
    	questions.add(q2);
    	questions.add(q3);
    	questions.add(q4);
    	questions.add(q5);
    	
    	return questions;
    }
    
    private List<Integer> getQuestionsIdStubData() {
    	List<Integer> questionsId = new ArrayList<Integer>();
    	
    	Integer qid1 = 101;
    	Integer qid2 = 102;
    	Integer qid3 = 103;
    	Integer qid4 = 104;
    	Integer qid5 = 105;
    	
    	questionsId.add(qid1);
    	questionsId.add(qid2);
    	questionsId.add(qid3);
    	questionsId.add(qid4);
    	questionsId.add(qid5);
    	
    	return questionsId;
    }
    
    /*private java.util.Map<String, String> getPlayerAnswersStubData() {
    	java.util.Map<String, String> playerAnswers = new HashMap<String, String>();
    	playerAnswers.put("101", "201");
    	playerAnswers.put("102", "202");
    	playerAnswers.put("103", "203");
    	playerAnswers.put("104", "204");
    	playerAnswers.put("105", "205");
    	return playerAnswers;
    }*/
    
    private Phase getPhaseOfTheTestStubData() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	Map map = new Map();
    	map.setGame(game);
    	
    	Phase phaseOfTheTest = new Phase();
    	phaseOfTheTest.setId(40);
    	phaseOfTheTest.setMap(map);
    	
        return phaseOfTheTest;
    }
    /* stubs - end */
    
    /* listGames - begin */
	@Test
	public void listGames_WhenListAllGames_OpenGamesPageAndListAllGames() throws Exception {
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
	public void listLevelsOfTheGame_WhenThePlayerHasNeverPlayedAnyPhaseOfTheGame_OpenLevelPageAndListLevelsWithOnlyTheFirstOneOpened() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = GAME_NAME_EXISTENT;
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(getCurrentUserStubData());
        
        when(playerPhaseService.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT)).thenReturn(null);
        
        when(levelService.flagLevelsToOpenedOrNot(GAME_ID_EXISTENT, 1)).thenReturn(getLevelListStubData());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
	        .andExpect(status().isOk())
	        .andExpect(view().name("games/level"))
	        .andExpect(forwardedUrl("games/level"))
	        .andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
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
	public void listLevelsOfTheGame_WhenThePlayerHasAlreadyPlayedAtLeastOnePhaseOfTheGame_OpenLevelsPageListLevelsAndShowFirstLevelAsOpened() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = GAME_NAME_EXISTENT;
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(getCurrentUserStubData());
		
        when(playerPhaseService.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT)).thenReturn(getLastPhaseCompletedStubData());
        
        when(phaseService.findLastPhaseOfTheLevel(GAME_ID_EXISTENT, 1)).thenReturn(getLastPhaseOfTheLevelStubData());
        
        when(levelService.flagLevelsToOpenedOrNot(GAME_ID_EXISTENT, 1)).thenReturn(getLevelListStubData());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
	        .andExpect(status().isOk())
	        .andExpect(view().name("games/level"))
	        .andExpect(forwardedUrl("games/level"))
	        .andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
	        .andExpect(model().attribute("levels", hasSize(5)))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(1)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        /*.andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(2)),
	                        hasProperty("opened", is(true))
	                )
	        )))*/
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
	public void listLevelsOfTheGame_WhenThePlayerHasPlayedTheLastPhaseOfTheALevel_OpenLevelsPageListLevelsAndShowTheNextLevelAsOpened() throws Exception {
		String uri = "/games/{gameName}";
		String gameName = GAME_NAME_EXISTENT;
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(getCurrentUserStubData());
		
        when(playerPhaseService.findLastPhaseCompleted(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT)).thenReturn(getLastPhaseCompletedLastPhaseOfTheLevelStubData());
        
        when(phaseService.findLastPhaseOfTheLevel(GAME_ID_EXISTENT, 1)).thenReturn(getLastPhaseOfTheLevelStubData());
        
        when(levelService.flagLevelsToOpenedOrNot(GAME_ID_EXISTENT, 2)).thenReturn(getLevelListStubData());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName))
	        .andExpect(status().isOk())
	        .andExpect(view().name("games/level"))
	        .andExpect(forwardedUrl("games/level"))
	        .andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
	        .andExpect(model().attribute("levels", hasSize(5)))
	        .andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(1)),
	                        hasProperty("opened", is(true))
	                )
	        )))
	        /*.andExpect(model().attribute("levels", hasItem(
	                allOf(
	                        hasProperty("order", is(2)),
	                        hasProperty("opened", is(true))
	                )
	        )))*/
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
	
	/* listPhasesOfTheMap - begin */
	@Test
	public void listPhasesOfTheMap_WhenTheGameDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}";
		String gameName = "inexistent";
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		
		when(gameService.findByNamelink(gameName)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
        verifyNoMoreInteractions(gameService);
	}
	
	@Test
	public void listPhasesOfTheMap_WhenTheLevelDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 0; // Level inexistent.
		Integer mapOrder = 1;
		
		when(gameService.findByNamelink(gameName)).thenReturn(getEntityStubData());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		
		levelOrder = 6; // Level inexistent.
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
    		.andExpect(status().is3xxRedirection())
    		.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void listPhasesOfTheMap_WhenTheMapDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 0; // Map inexistent.
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
	}
	
	@Test
	public void listPhasesOfTheMap_WhenThereAreNotPhasesInTheMap_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(getCurrentUserStubData());
		
		PlayerPhase lastPhaseCompleted = new PlayerPhase();
		when(playerPhaseService.findLastPhaseCompleted(PLAYER_ID_EXISTENT, entity.getId())).thenReturn(lastPhaseCompleted);
		
		when(phaseService.findPhasesCheckedByMap(map, lastPhaseCompleted)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(playerPhaseService, times(1)).findLastPhaseCompleted(PLAYER_ID_EXISTENT, entity.getId());
        verifyNoMoreInteractions(playerPhaseService);
        verify(phaseService, times(1)).findPhasesCheckedByMap(map, lastPhaseCompleted);
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void listPhasesOfTheMap_WhenPlayerCanNotAccessTheMap_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		PlayerPhase lastPhaseCompleted = new PlayerPhase();
		when(playerPhaseService.findLastPhaseCompleted(PLAYER_ID_EXISTENT, entity.getId())).thenReturn(lastPhaseCompleted);
		
		when(phaseService.findPhasesCheckedByMap(map, lastPhaseCompleted)).thenReturn(getPhasesCheckedByMapStubData());
		
		when(mapService.playerCanAccessThisMap(map, currentUser.getUser())).thenReturn(false);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verify(playerPhaseService, times(1)).findLastPhaseCompleted(PLAYER_ID_EXISTENT, entity.getId());
        verifyNoMoreInteractions(playerPhaseService);
        verify(phaseService, times(1)).findPhasesCheckedByMap(map, lastPhaseCompleted);
        verifyNoMoreInteractions(phaseService);
        verify(mapService, times(1)).playerCanAccessThisMap(map, currentUser.getUser());
        verifyNoMoreInteractions(mapService);
	}
	
	@Test
	public void listPhasesOfTheMap_WhenEverythingIsOK_OpenMapPage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setId(MAP_ID_EXISTENT);
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		PlayerPhase lastPhaseCompleted = new PlayerPhase();
		when(playerPhaseService.findLastPhaseCompleted(PLAYER_ID_EXISTENT, entity.getId())).thenReturn(lastPhaseCompleted);
		
		when(phaseService.findPhasesCheckedByMap(map, lastPhaseCompleted)).thenReturn(getPhasesCheckedByMapStubData());
		
		when(mapService.playerCanAccessThisMap(map, currentUser.getUser())).thenReturn(true);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("games/map"))
        	.andExpect(forwardedUrl("games/map"))
        	.andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
        	.andExpect(model().attribute("map", hasProperty("id", is(MAP_ID_EXISTENT))))
        	.andExpect(model().attribute("phases", hasSize(30)));
	}
	/* listPhasesOfTheMap - end */
	
	/* initPhase - begin */
	@Test
	public void initPhase_WhenTheGameDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = "inexistent";
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		when(gameService.findByNamelink(gameName)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
        verifyNoMoreInteractions(gameService);
	}
	
	@Test
	public void initPhase_WhenTheMapDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 0; // Map inexistent.
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
	}
	
	@Test
	public void initPhase_WhenThePhaseDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 0; // Phase inexistent. 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initPhase_WhenThePlayerDoesntHavePermissionToAccessThePhase_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(false);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initPhase_WhenTheContentDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		when(contentService.findByPhaseAndOrder(phase.getId(), 1)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verifyNoMoreInteractions(phaseService);
        verify(contentService, times(1)).findByPhaseAndOrder(phase.getId(), 1);
        verifyNoMoreInteractions(contentService);
	}
	
	@Test
	public void initPhase_WhenThePlayerDoesntHaveCreditsAndHeIsNotTryingToAccessAPhaseThatHeHasAlreadyFinished_OpenBuyCreditsPageAndShowMessage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setGame(entity);
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		phase.setOrder(5);
		phase.setMap(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		Content content = new Content();
		content.setId(378);
		when(contentService.findByPhaseAndOrder(phase.getId(), 1)).thenReturn(content);
		
		when(playerService.playerHasCredits(currentUser.getUser(), phase)).thenReturn(false);
		
		Phase lastPhaseDone = new Phase();
		lastPhaseDone.setOrder(4);
		when(phaseService.findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId())).thenReturn(lastPhaseDone);
		
		ResourceBundle messagesResourceBundle = ResourceBundle.getBundle("messages/messages", Locale.getDefault());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("buycredits"))
        	.andExpect(model().attribute("msg", messagesResourceBundle.getString("msg.credits.insufficient")));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verify(contentService, times(1)).findByPhaseAndOrder(phase.getId(), 1);
        verifyNoMoreInteractions(contentService);
        verify(playerService, times(1)).playerHasCredits(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerService);
        verify(phaseService, times(1)).findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId());
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initPhase_WhenThePlayerDoesntHaveCreditsButHeIsTryingToAccessAPhaseThatHeHasAlreadyFinished_OpenPhaseContentPage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setId(52);
		map.setGame(entity);
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		phase.setId(23);
		phase.setOrder(5); // The player is trying to access this phase.
		phase.setMap(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		Content content = new Content();
		content.setId(378);
		when(contentService.findByPhaseAndOrder(phase.getId(), 1)).thenReturn(content);
		
		when(playerService.playerHasCredits(currentUser.getUser(), phase)).thenReturn(false);
		
		Phase lastPhaseDone = new Phase();
		lastPhaseDone.setOrder(5); // Last phase done by this player.
		when(phaseService.findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId())).thenReturn(lastPhaseDone);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("games/phaseContent"))
        	.andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
        	.andExpect(model().attribute("map", hasProperty("id", is(map.getId()))))
        	.andExpect(model().attribute("phase", hasProperty("id", is(phase.getId()))))
        	.andExpect(model().attribute("content", hasProperty("id", is(content.getId()))));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verify(contentService, times(1)).findByPhaseAndOrder(phase.getId(), 1);
        verifyNoMoreInteractions(contentService);
        verify(playerService, times(1)).playerHasCredits(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerService);
        verify(phaseService, times(1)).findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId());
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initPhase_WhenEverythingIsOK_OpenPhaseContentPage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setId(52);
		map.setGame(entity);
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		phase.setId(23);
		phase.setOrder(5); // The player is trying to access this phase.
		phase.setMap(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		Content content = new Content();
		content.setId(378);
		when(contentService.findByPhaseAndOrder(phase.getId(), 1)).thenReturn(content);
		
		when(playerService.playerHasCredits(currentUser.getUser(), phase)).thenReturn(true);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("games/phaseContent"))
        	.andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
        	.andExpect(model().attribute("map", hasProperty("id", is(map.getId()))))
        	.andExpect(model().attribute("phase", hasProperty("id", is(phase.getId()))))
        	.andExpect(model().attribute("content", hasProperty("id", is(content.getId()))));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verify(contentService, times(1)).findByPhaseAndOrder(phase.getId(), 1);
        verifyNoMoreInteractions(contentService);
        verify(playerService, times(1)).playerHasCredits(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerService);
	}
	/* initPhase - end */
	
	/* initTest - begin */
	@Test
	public void initTest_WhenTheGameDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = "inexistent";
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		when(gameService.findByNamelink(gameName)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
        verifyNoMoreInteractions(gameService);
	}
	
	@Test
	public void initTest_WhenTheMapDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 0; // Map inexistent.
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
	}
	
	@Test
	public void initTest_WhenThePhaseDoesntExist_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 0; // Phase inexistent. 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initTest_WhenThePlayerHasAlreadyPassedTheTest_RedirectToGamePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser())).thenReturn(true);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/games/" + gameName));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verifyNoMoreInteractions(phaseService);
        verify(playerPhaseService, times(1)).isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser());
        verifyNoMoreInteractions(playerPhaseService);
	}
	
	@Test
	public void initTest_WhenThePlayerDoesntHavePermissionToAccessThePhase_RedirectToHomePage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser())).thenReturn(false);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(false);
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(playerPhaseService, times(1)).isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser());
        verifyNoMoreInteractions(playerPhaseService);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initTest_WhenThePlayerDoesntHaveCreditsAndHeIsNotTryingToAccessAPhaseThatHeHasAlreadyFinished_OpenBuyCreditsPageAndShowMessage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setGame(entity);
		
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		phase.setOrder(5); // The player is trying to access this phase.
		phase.setMap(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser())).thenReturn(false);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		when(playerService.playerHasCredits(currentUser.getUser(), phase)).thenReturn(false);
		
		Phase lastPhaseDone = new Phase();
		lastPhaseDone.setOrder(4); // Last phase done by this player.
		when(phaseService.findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId())).thenReturn(lastPhaseDone);
		
		ResourceBundle messagesResourceBundle = ResourceBundle.getBundle("messages/messages", Locale.getDefault());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("buycredits"))
        	.andExpect(model().attribute("msg", messagesResourceBundle.getString("msg.credits.insufficient")));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(playerPhaseService, times(1)).isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser());
        verifyNoMoreInteractions(playerPhaseService);
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verify(playerService, times(1)).playerHasCredits(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerService);
        verify(phaseService, times(1)).findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId());
        verifyNoMoreInteractions(phaseService);
	}
	
	@Test
	public void initTest_WhenThePlayerDoesntHaveCreditsButHeIsTryingToAccessAPhaseThatHeHasAlreadyFinished_OpenPhaseTestPage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setGame(entity);
		
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		phase.setOrder(5); // The player is trying to access this phase.
		phase.setMap(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser())).thenReturn(false);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		when(playerService.playerHasCredits(currentUser.getUser(), phase)).thenReturn(false);
		
		Phase lastPhaseDone = new Phase();
		lastPhaseDone.setOrder(6); // Last phase done by this player.
		when(phaseService.findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId())).thenReturn(lastPhaseDone);
		
		when(playerPhaseService.setTestAttempt(currentUser.getUser(), phase)).thenReturn(new PlayerPhase());
		
		Content content = new Content();
		content.setId(224);
		when(contentService.findByPhaseAndOrder(phase.getId(), 0)).thenReturn(content);

		when(questionService.findQuestionsByContent(content.getId())).thenReturn(getQuestionsByContentStubData());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("games/phaseTest"))
        	.andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
        	.andExpect(model().attribute("map", hasProperty("id", is(map.getId()))))
        	.andExpect(model().attribute("phase", hasProperty("id", is(phase.getId()))))
        	.andExpect(model().attribute("questions", hasSize(5)))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(101)),
                            hasProperty("name", is("Question 01")),
                            hasProperty("order", is(1)),
                            hasProperty("answers", hasSize(4) )
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(102)),
                            hasProperty("name", is("Question 02")),
                            hasProperty("order", is(2))
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(103)),
                            hasProperty("name", is("Question 03")),
                            hasProperty("order", is(3))
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(104)),
                            hasProperty("name", is("Question 04")),
                            hasProperty("order", is(4))
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(105)),
                            hasProperty("name", is("Question 05")),
                            hasProperty("order", is(5))
                    )
            )))
            .andExpect(request().sessionAttribute("questionsId", hasSize(5)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(101)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(102)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(103)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(104)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(105)));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(playerPhaseService, times(1)).isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser());
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verify(playerService, times(1)).playerHasCredits(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerService);
        verify(phaseService, times(1)).findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, phase.getMap().getGame().getId());
        verifyNoMoreInteractions(phaseService);
        verify(playerPhaseService).setTestAttempt(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerPhaseService);
        verify(contentService, times(1)).findByPhaseAndOrder(phase.getId(), 0);
        verifyNoMoreInteractions(contentService);
        verify(questionService, times(1)).findQuestionsByContent(content.getId());
        verifyNoMoreInteractions(questionService);
	}
	
	@Test
	public void initTest_WhenEverythingIsOK_OpenPhaseTestPage() throws Exception {
		String uri = "/games/{gameName}/{levelOrder}/{mapOrder}/{phaseOrder}/test";
		String gameName = GAME_NAME_EXISTENT;
		Integer levelOrder = 1;
		Integer mapOrder = 1;
		Integer phaseOrder = 1; 
		
		Game entity = getEntityStubData();
		when(gameService.findByNamelink(gameName)).thenReturn(entity);
		
		Map map = new Map();
		map.setGame(entity);
		
		when(mapService.findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder)).thenReturn(map);
		
		Phase phase = new Phase();
		phase.setOrder(5); // The player is trying to access this phase.
		phase.setMap(map);
		
		when(phaseService.findByMapAndOrder(map.getId(), phaseOrder)).thenReturn(phase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
		when(authenticationMock.getPrincipal()).thenReturn(currentUser);
		
		when(playerPhaseService.isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser())).thenReturn(false);
		
		when(phaseService.playerCanAccessThisPhase(phase, currentUser.getUser())).thenReturn(true);
		
		when(playerService.playerHasCredits(currentUser.getUser(), phase)).thenReturn(true);
		
		when(playerPhaseService.setTestAttempt(currentUser.getUser(), phase)).thenReturn(new PlayerPhase());
		
		Content content = new Content();
		content.setId(224);
		when(contentService.findByPhaseAndOrder(phase.getId(), 0)).thenReturn(content);

		when(questionService.findQuestionsByContent(content.getId())).thenReturn(getQuestionsByContentStubData());
		
		mvc.perform(MockMvcRequestBuilders.get(uri, gameName, levelOrder, mapOrder, phaseOrder))
        	.andExpect(status().isOk())
        	.andExpect(view().name("games/phaseTest"))
        	.andExpect(model().attribute("game", hasProperty("id", is(GAME_ID_EXISTENT))))
        	.andExpect(model().attribute("map", hasProperty("id", is(map.getId()))))
        	.andExpect(model().attribute("phase", hasProperty("id", is(phase.getId()))))
        	.andExpect(model().attribute("questions", hasSize(5)))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(101)),
                            hasProperty("name", is("Question 01")),
                            hasProperty("order", is(1)),
                            hasProperty("answers", hasSize(4) )
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(102)),
                            hasProperty("name", is("Question 02")),
                            hasProperty("order", is(2))
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(103)),
                            hasProperty("name", is("Question 03")),
                            hasProperty("order", is(3))
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(104)),
                            hasProperty("name", is("Question 04")),
                            hasProperty("order", is(4))
                    )
            )))
            .andExpect(model().attribute("questions", hasItem(
                    allOf(
                            hasProperty("id", is(105)),
                            hasProperty("name", is("Question 05")),
                            hasProperty("order", is(5))
                    )
            )))
            .andExpect(request().sessionAttribute("questionsId", hasSize(5)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(101)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(102)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(103)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(104)))
            .andExpect(request().sessionAttribute("questionsId", hasItem(105)));
		
		verify(gameService, times(1)).findByNamelink(gameName);
		verifyNoMoreInteractions(gameService);
		verify(mapService, times(1)).findByGameLevelAndOrder(entity.getId(), levelOrder, mapOrder);
        verifyNoMoreInteractions(mapService);
        verify(phaseService, times(1)).findByMapAndOrder(map.getId(), phaseOrder);
        verify(playerPhaseService, times(1)).isPhaseAlreadyCompletedByPlayer(phase, currentUser.getUser());
        verify(phaseService, times(1)).playerCanAccessThisPhase(phase, currentUser.getUser());
        verifyNoMoreInteractions(phaseService);
        verify(playerService, times(1)).playerHasCredits(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerService);
        verify(playerPhaseService).setTestAttempt(currentUser.getUser(), phase);
        verifyNoMoreInteractions(playerPhaseService);
        verify(contentService, times(1)).findByPhaseAndOrder(phase.getId(), 0);
        verifyNoMoreInteractions(contentService);
        verify(questionService, times(1)).findQuestionsByContent(content.getId());
        verifyNoMoreInteractions(questionService);
	}
	/* initTest - end */
	
	/* showResultTest - begin */
	@Test
	public void showResultTest_WhenQuestionListIsEmpty_RedirectToHomePage() throws Exception {
		String uri = "/games/result";
		
		// questionsId is null.
		mvc.perform(MockMvcRequestBuilders.post(uri))
        	.andExpect(status().is3xxRedirection())
        	.andExpect(view().name("redirect:/"));
		
		// questionsId is not null, but is empty.
		mvc.perform(
			MockMvcRequestBuilders.post(uri)
			.sessionAttr("questionsId", new ArrayList<Integer>())
		)
    	.andExpect(status().is3xxRedirection())
    	.andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void showResultTest_WhenGradeLessThanMinimumRequired_OpenResultTestPageAndShowPlayersGrade() throws Exception {
		String uri = "/games/result";
		
		List<Integer> questionsId = getQuestionsIdStubData();
		java.util.Map<String, String> playerAnswers = new HashMap<String, String>(); //getPlayerAnswersStubData();
		
		when(gameService.calculateGrade(questionsId, playerAnswers)).thenReturn(50);
		
		Phase currentPhase = getPhaseOfTheTestStubData();
		when(gameService.getPhaseOfTheTest(questionsId)).thenReturn(currentPhase);
		
		mvc.perform(
			MockMvcRequestBuilders.post(uri)
			.sessionAttr("questionsId", questionsId)
			//.param("playerAnswers", mapToJson(playerAnswers))
		)
    	.andExpect(status().isOk())
    	.andExpect(view().name("games/resultTest"))
    	.andExpect(model().attribute("grade", 50))
    	.andExpect(model().attribute("phase", currentPhase));
	}
	
	@Test
	public void showResultTest_WhenGradeIsEnoughAndThePlayerHasJustFinishedTheLastPhaseOfTheLastMapOfTheLevel_OpenEndoflevelPageAndShowPlayersGrade() throws Exception {
		String uri = "/games/result";
		
		List<Integer> questionsId = getQuestionsIdStubData();
		java.util.Map<String, String> playerAnswers = new HashMap<String, String>(); //getPlayerAnswersStubData();
		
		int grade = 80;
		when(gameService.calculateGrade(questionsId, playerAnswers)).thenReturn(grade);
		
		Phase currentPhase = getPhaseOfTheTestStubData();
		when(gameService.getPhaseOfTheTest(questionsId)).thenReturn(currentPhase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
        Player player = currentUser.getUser();
        player.setScore(100);
		
        PlayerPhase playerPhase = new PlayerPhase();
        playerPhase.setNumAttempts(2);
        when(playerPhaseService.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, currentPhase.getId(), 2)).thenReturn(playerPhase);
        
        when(gameService.calculateScore(playerPhase.getNumAttempts(), grade)).thenReturn(65);
        
        when(playerService.findOne(PLAYER_ID_EXISTENT)).thenReturn(player);
        
        when(playerService.removeOneCreditFromPlayer(player, currentPhase.getMap().getGame())).thenReturn(player);
        
        when(playerPhaseService.save(playerPhase)).thenReturn(playerPhase);
        
        Map map = new Map();
        map.setLevelCompleted(true);
        when(mapService.findCurrentMap(currentPhase.getMap().getGame(), playerPhase)).thenReturn(map);
        
        ResourceBundle applicationResourceBundle = ResourceBundle.getBundle("application", Locale.getDefault());
        when(playerService.addCreditsToPlayer(PLAYER_ID_EXISTENT, Integer.parseInt(applicationResourceBundle.getString("credits.levelCompleted")))).thenReturn(player);

		mvc.perform(
			MockMvcRequestBuilders.post(uri)
			.sessionAttr("questionsId", questionsId)
			//.param("playerAnswers", mapToJson(playerAnswers))
		)
    	.andExpect(status().isOk())
    	.andExpect(view().name("games/endoflevel"))
    	.andExpect(model().attribute("grade", grade));
	}
	
	@Test
	public void showResultTest_WhenGradeIsEnoughAndThePlayerHasJustFinishedTheLastPhaseOfTheLastMapOfTheLastLevelOfTheGame_OpenEndofgamePageAndShowPlayersGrade() throws Exception {
		String uri = "/games/result";
		
		List<Integer> questionsId = getQuestionsIdStubData();
		java.util.Map<String, String> playerAnswers = new HashMap<String, String>(); //getPlayerAnswersStubData();
		
		int grade = 80;
		when(gameService.calculateGrade(questionsId, playerAnswers)).thenReturn(grade);
		
		Phase currentPhase = getPhaseOfTheTestStubData();
		when(gameService.getPhaseOfTheTest(questionsId)).thenReturn(currentPhase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
        Player player = currentUser.getUser();
        player.setScore(100);
		
        PlayerPhase playerPhase = new PlayerPhase();
        playerPhase.setNumAttempts(2);
        when(playerPhaseService.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, currentPhase.getId(), 2)).thenReturn(playerPhase);
        
        when(gameService.calculateScore(playerPhase.getNumAttempts(), grade)).thenReturn(65);
        
        when(playerService.findOne(PLAYER_ID_EXISTENT)).thenReturn(player);
        
        when(playerService.removeOneCreditFromPlayer(player, currentPhase.getMap().getGame())).thenReturn(player);
        
        when(playerPhaseService.save(playerPhase)).thenReturn(playerPhase);
        
        Map map = new Map();
        map.setLevelCompleted(false);
        map.setGameCompleted(true);
        when(mapService.findCurrentMap(currentPhase.getMap().getGame(), playerPhase)).thenReturn(map);
        
        ResourceBundle applicationResourceBundle = ResourceBundle.getBundle("application", Locale.getDefault());
        when(playerService.addCreditsToPlayer(PLAYER_ID_EXISTENT, Integer.parseInt(applicationResourceBundle.getString("credits.gameCompleted")))).thenReturn(player);

		mvc.perform(
			MockMvcRequestBuilders.post(uri)
			.sessionAttr("questionsId", questionsId)
			//.param("playerAnswers", mapToJson(playerAnswers))
		)
    	.andExpect(status().isOk())
    	.andExpect(view().name("games/endofgame"))
    	.andExpect(model().attribute("grade", grade));
	}
	
	@Test
	public void showResultTest_WhenGradeIsEnough_OpenResultTestPageAndShowPlayersGrade() throws Exception {
		String uri = "/games/result";
		
		List<Integer> questionsId = getQuestionsIdStubData();
		java.util.Map<String, String> playerAnswers = new HashMap<String, String>(); //getPlayerAnswersStubData();
		
		int grade = 80;
		when(gameService.calculateGrade(questionsId, playerAnswers)).thenReturn(grade);
		
		Phase currentPhase = getPhaseOfTheTestStubData();
		when(gameService.getPhaseOfTheTest(questionsId)).thenReturn(currentPhase);
		
		// Emulating a logged in user.
		SecurityContext securityContextMock = mock(SecurityContext.class);
		Authentication authenticationMock = mock(Authentication.class);
		PowerMockito.mockStatic(SecurityContextHolder.class);
		
		when(SecurityContextHolder.getContext()).thenReturn(securityContextMock);
		when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		CurrentUser currentUser = getCurrentUserStubData();
        when(authenticationMock.getPrincipal()).thenReturn(currentUser);
        Player player = currentUser.getUser();
        player.setScore(100);
		
        PlayerPhase playerPhase = new PlayerPhase();
        playerPhase.setNumAttempts(2);
        when(playerPhaseService.findByPlayerPhaseAndStatus(PLAYER_ID_EXISTENT, currentPhase.getId(), 2)).thenReturn(playerPhase);
        
        when(gameService.calculateScore(playerPhase.getNumAttempts(), grade)).thenReturn(65);
        
        when(playerService.findOne(PLAYER_ID_EXISTENT)).thenReturn(player);
        
        when(playerService.removeOneCreditFromPlayer(player, currentPhase.getMap().getGame())).thenReturn(player);
        
        when(playerPhaseService.save(playerPhase)).thenReturn(playerPhase);
        
        Map map = new Map();
        map.setLevelCompleted(false);
        map.setGameCompleted(false);
        when(mapService.findCurrentMap(currentPhase.getMap().getGame(), playerPhase)).thenReturn(map);
        
        when(phaseService.findPhasesCheckedByMap(map, playerPhase)).thenReturn(getPhasesCheckedByMapStubData());

		mvc.perform(
			MockMvcRequestBuilders.post(uri)
			.sessionAttr("questionsId", questionsId)
			//.param("playerAnswers", mapToJson(playerAnswers))
		)
    	.andExpect(status().isOk())
    	.andExpect(view().name("games/resultTest"))
    	.andExpect(model().attribute("grade", grade))
    	.andExpect(model().attribute("phase", hasProperty("id", is(2))));
	}
	/* showResultTest - end */
	
	//calculatePlayerScoreAfterPassTheTest
	
	//setNextPhase
}