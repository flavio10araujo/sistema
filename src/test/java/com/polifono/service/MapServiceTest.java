package com.polifono.service;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.service.impl.MapServiceImpl;

/**
 * 
 * Unit test methods for the MapService.
 * 
 */
@Transactional
public class MapServiceTest extends AbstractTest {

    private IMapService service;
    
    @Mock
	private IPhaseService phaseService;
	
	private final Integer GAME_ID_EXISTENT = 1;
	private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer LEVEL_ID_EXISTENT = 1;
	private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;
	private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
		service = new MapServiceImpl(phaseService);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* playerCanAccessThisMap - begin */
    /*@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessTheFirstMapOfTheFirstLevel_returnTrue() {
    	Map map = new Map();
    	Level level = new Level();
    	level.setOrder(1);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }

    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapDifferentOfTheFirstMapOfTheFirstLevelAndThePlayerHasNeverFinishedAPhaseOfThisGame_returnFalse() {
		Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setOrder(2);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	player.setId(1);
		
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(null);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapInAPreviousLevelThanTheLastPhaseDonesLevel_returnTrue() {
    	Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setOrder(2);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setOrder(3);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAPreviousMapAtTheSameLevelOfTheLastPhaseDone_returnTrue() {
    	Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setOrder(3);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setOrder(3);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(2);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);

    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }*/

    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapButTheLastPhaseDoneIsNotTheLastPhaseOfTheLevel_returnFalse() {
    	Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setOrder(3);
    	map.setLevel(level);
    	map.setOrder(1);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setId(2);
    	levelLastPhaseDone.setOrder(2);
    	
    	Game gameLastPhaseDone = new Game();
    	gameLastPhaseDone.setId(1);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(1);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	mapLastPhaseDone.setGame(gameLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setId(59);
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Phase lastPhaseOfTheLevel = new Phase();
    	lastPhaseOfTheLevel.setId(60);
    	
    	when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(lastPhaseOfTheLevel);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
    
    /*@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelAndItIsTheNext_returnTrue() {
    	//fazer um Mock para retornar Phase lastPhaseDone com uma fase no terceiro nível e primeiro mapa em phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId());
    	
    	Map map = new Map();
    	Level level = new Level();
    	level.setOrder(3);
    	map.setLevel(level);
    	map.setOrder(2);
    }*/
    
    /*@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelButItIsNotTheNext_returnFalse() {
    	//fazer um Mock para retornar Phase lastPhaseDone com uma fase no terceiro nível e primeiro mapa em phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId());
    	
    	Map map = new Map();
    	Level level = new Level();
    	level.setOrder(3);
    	map.setLevel(level);
    	map.setOrder(3);
    }*/
    
    /*@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsTheFristOfTheNextLevel_returnTrue() {
    	//fazer um Mock para retornar Phase lastPhaseDone com uma fase no terceiro nível e primeiro mapa em phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId());
    	
    	Map map = new Map();
    	Level level = new Level();
    	level.setOrder(4);
    	map.setLevel(level);
    	map.setOrder(1);
    }*/
    
    /*@Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapINotTheFristOfTheNextLevel_returnFalse() {
    	//fazer um Mock para retornar Phase lastPhaseDone com uma fase no terceiro nível e primeiro mapa em phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId());
    	
    	Map map = new Map();
    	Level level = new Level();
    	level.setOrder(4);
    	map.setLevel(level);
    	map.setOrder(2);
    }*/
    /* playerCanAccessThisMap - end */
}