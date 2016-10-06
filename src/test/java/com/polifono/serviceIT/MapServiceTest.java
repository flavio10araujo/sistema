package com.polifono.serviceIT;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.PlayerPhase;
import com.polifono.repository.IMapRepository;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.impl.MapServiceImpl;

/**
 * 
 * Unit test methods for the MapService.
 * 
 */
@Transactional
public class MapServiceTest extends AbstractTest {

	private IMapService service;
	
	@Autowired
	private IMapRepository repository;
    
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
		service = new MapServiceImpl(repository, phaseService);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin 
    @Test
    public void save_createMap() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	Level level = new Level();
    	level.setId(LEVEL_ID_EXISTENT);
    	
    	Map map = new Map();
    	map.setName("Map Test");
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Map entitySaved = service.save(map);
    	Map entity = service.findOne(entitySaved.getId()); 

    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    }

    @Test
    public void save_updateMap() {
    	Integer id = new Integer(MAP_ID_EXISTENT);
        Map entity = service.findOne(id);

        // Changing all possible fields.
        // id - not possible to change.
        // game - it's possible to change, but it's hard to test because the system may have only one game.
        //Game newGame = new Game();
        //newGame.setId(GAME_ID_EXISTENT + 1);
        //entity.setGame(newGame);
        Level newLevel = new Level();
        newLevel.setId(LEVEL_ID_EXISTENT + 1); // too risky, because I can't assure that this level really exists.
        entity.setLevel(newLevel);
        entity.setName("Name Changed");
        entity.setOrder(entity.getOrder() + 1);

        // Saving the changes.
        Map changedEntity = service.save(entity);

        Assert.assertNotNull("failure - expected not null", changedEntity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), changedEntity.getId());
        
        // Get the entity in the database with the changes.
        Map updatedEntity = service.findOne(changedEntity.getId());
        
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
        Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
        Assert.assertEquals("failure - expected game attribute match", entity.getGame().getId(), updatedEntity.getGame().getId());
        Assert.assertEquals("failure - expected level attribute match", entity.getLevel().getId(), updatedEntity.getLevel().getId());
    }
     save - end */
    
    /* delete - begin 
    @Test
    public void delete_MapExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(MAP_ID_EXISTENT));
    	Map entity = service.findOne(MAP_ID_EXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void delete_MapInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(MAP_ID_INEXISTENT));
    }
     delete - end */
    
    /* findOne - begin 
    @Test
    public void findOne_MapExistentButReturnNull_ExceptionThrown() {
        Integer id = new Integer(MAP_ID_EXISTENT);
        Map entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findOne_MapExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(MAP_ID_EXISTENT);
        Map entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    @Test
    public void findOne_MapInexistent_ReturnNull() {
        Integer id = MAP_ID_INEXISTENT;
    	Map entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
     findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_ListIsNullOrEmpty_ExceptionThrown() {
    	List<Map> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findMapsByGame - begin 
    @Test
    public void findMapsByGame_SearchGameExistent_ReturnList() {
    	List<Map> list = service.findMapsByGame(GAME_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findMapsByGame_SearchGameInexistent_ReturnListEmpty() {
    	List<Map> list = service.findMapsByGame(GAME_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
     findMapsByGame - end */
    
    /* findMapsByGameAndLevel - begin 
    @Test
    public void findMapsByGameAndLevel_SearchGameAndLevelExistents_ReturnList() {
    	List<Map> list = service.findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findMapsByGameAndLevel_SearchGameAndLevelInexistents_ReturnListEmpty() {
    	List<Map> list = service.findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findMapsByGameAndLevel_SearchGameExistentButLevelInexistent_ReturnListEmpty() {
    	List<Map> list = service.findMapsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findMapsByGameAndLevel_SearchLevelExistentButGaemInexistent_ReturnListEmpty() {
    	List<Map> list = service.findMapsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
     findMapsByGameAndLevel - end */
    
    /* findByGameAndLevel - begin 
    @Test
    public void findByGameAndLevel_SearchGameAndLevelExistents_ReturnItem() {
    	Map entity = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findByGameAndLevel_SearchGameAndLevelInexistents_ReturnNull() {
    	Map entity = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
    }
     findByGameAndLevel - end */
    
    /* findByGameLevelAndOrder - begin 
    @Test
    public void findByGameLevelAndOrder_SearchGameLevelAndOrderExistents_ReturnItem() {
    	Map entity = service.findByGameLevelAndOrder(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT, 1);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findByGameLevelAndOrder_SearchGameLevelAndOrderInexistents_ReturnNull() {
    	Map entity = service.findByGameLevelAndOrder(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT, 0);
        Assert.assertNull("failure - expected null", entity);
    }
     findByGameLevelAndOrder - end */
    
    /* findNextMapSameLevel - begin 
    @Test
    public void findNextMapSameLevel_WhenNextMapExists_ReturnItem() {
    	Map mapCurrent = service.findOne(MAP_ID_EXISTENT);
    	
    	Map nextMapSameLevel = new Map();
    	nextMapSameLevel.setGame(mapCurrent.getGame());
    	nextMapSameLevel.setLevel(mapCurrent.getLevel());
    	nextMapSameLevel.setName("Next Map Same Level");
    	nextMapSameLevel.setOrder(mapCurrent.getOrder() + 1);
    	service.save(nextMapSameLevel);
    	
    	Map entity = service.findNextMapSameLevel(mapCurrent);
    	
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertEquals(nextMapSameLevel.getId(), entity.getId());
    }
     findNextMapSameLevel - end */
    
    /* playerCanAccessThisMap - begin 
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessTheFirstMapOfTheFirstLevel_returnTrue() {
    	Level level = new Level();
    	level.setOrder(1);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }

    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapDifferentOfTheFirstMapOfTheFirstLevelAndThePlayerHasNeverFinishedAPhaseOfThisGame_returnFalse() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(2);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	player.setId(1);
		
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(null);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapInAPreviousLevelThanTheLastPhaseDonesLevel_returnTrue() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(2);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
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
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(3);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
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
    }

    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapButTheLastPhaseDoneIsNotTheLastPhaseOfTheLevel_returnFalse() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(3);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
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
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelAndItIsTheNext_returnTrue() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(3);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(2);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setId(3);
    	levelLastPhaseDone.setOrder(3);
    	
    	Game gameLastPhaseDone = new Game();
    	gameLastPhaseDone.setId(1);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(1);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	mapLastPhaseDone.setGame(gameLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setId(60);
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Phase lastPhaseOfTheLevel = new Phase();
    	lastPhaseOfTheLevel.setId(60);
    	
    	when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(lastPhaseOfTheLevel);
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelButItIsNotTheNext_returnFalse() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(3);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(3);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setId(3);
    	levelLastPhaseDone.setOrder(3);
    	
    	Game gameLastPhaseDone = new Game();
    	gameLastPhaseDone.setId(1);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(1);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	mapLastPhaseDone.setGame(gameLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setId(60);
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Phase lastPhaseOfTheLevel = new Phase();
    	lastPhaseOfTheLevel.setId(60);
    	
    	when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(lastPhaseOfTheLevel);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsTheFirstOfTheNextLevel_returnTrue() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(4);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setId(3);
    	levelLastPhaseDone.setOrder(3);
    	
    	Game gameLastPhaseDone = new Game();
    	gameLastPhaseDone.setId(1);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(1);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	mapLastPhaseDone.setGame(gameLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setId(60);
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Phase lastPhaseOfTheLevel = new Phase();
    	lastPhaseOfTheLevel.setId(60);
    	
    	when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(lastPhaseOfTheLevel);
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapINotTheFirstOfTheNextLevel_returnFalse() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(4);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(2);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setId(3);
    	levelLastPhaseDone.setOrder(3);
    	
    	Game gameLastPhaseDone = new Game();
    	gameLastPhaseDone.setId(1);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(1);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	mapLastPhaseDone.setGame(gameLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setId(60);
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Phase lastPhaseOfTheLevel = new Phase();
    	lastPhaseOfTheLevel.setId(60);
    	
    	when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(lastPhaseOfTheLevel);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
    
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessMapButTheLevelOfThisMapIsNotTheNextLevel_returnFalse() {
    	Game game = new Game();
    	game.setId(1);
    	
    	Level level = new Level();
    	level.setOrder(5);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setGame(game);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	player.setId(1);
    	
    	Level levelLastPhaseDone = new Level();
    	levelLastPhaseDone.setId(3);
    	levelLastPhaseDone.setOrder(3);
    	
    	Game gameLastPhaseDone = new Game();
    	gameLastPhaseDone.setId(1);
    	
    	Map mapLastPhaseDone = new Map();
    	mapLastPhaseDone.setOrder(1);
    	mapLastPhaseDone.setLevel(levelLastPhaseDone);
    	mapLastPhaseDone.setGame(gameLastPhaseDone);
    	
    	Phase lastPhaseDone = new Phase();
    	lastPhaseDone.setId(60);
    	lastPhaseDone.setMap(mapLastPhaseDone);
    	
    	when(phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId())).thenReturn(lastPhaseDone);
    	
    	Phase lastPhaseOfTheLevel = new Phase();
    	lastPhaseOfTheLevel.setId(60);
    	
    	when(phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId())).thenReturn(lastPhaseOfTheLevel);
    	
    	Assert.assertFalse(service.playerCanAccessThisMap(map, player));
    }
     playerCanAccessThisMap - end */
    
    /* findCurrentMap - begin */
    @Test
    public void findCurrentMap_WhenPlayerHasNeverCompletedAnyPhaseOfTheGame_returnFirstMapOfTheFirstLevelOfTheGame() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = null;
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals("failure - expected map order attribute match", 1, entity.getOrder());
    	Assert.assertEquals("failure - expected level order attribute match", 1, entity.getLevel().getOrder());
    	Assert.assertEquals("failure - expected game id attribute match", game.getId(), entity.getGame().getId());
    }
    
    @Test
    public void findCurrentMap_WhenNextPhaseIsInTheSameMapThatTheLastPhaseCompleted_returnSameMapOfTheLastPhaseCompleted() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	Map map = new Map();
    	map.setId(3); // Map ID of the last phase completed.
    	Phase phase = new Phase();
    	phase.setOrder(10); // Last phase completed.
    	phase.setMap(map);
    	lastPhaseCompleted.setPhase(phase);
    	
    	Phase nextPhase = new Phase(); // Mocking the next phase.
    	nextPhase.setOrder(11);
    	
    	when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(nextPhase);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals("failure - expected map id attribute match", lastPhaseCompleted.getPhase().getMap().getId(), entity.getId());
    }
    
    @Test
    public void findCurrentMap_WhenNextPhaseIsInTheNextMapInTheSameLevel_returnMapWithNextOrderAndInTheSameLevel() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	Level level = new Level();
    	level.setOrder(1);
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	map.setOrder(1);
    	map.setLevel(level);
    	Phase phase = new Phase();
    	phase.setMap(map);
    	lastPhaseCompleted.setPhase(phase);

    	when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(null);
    	
    	Map nextMapSameLevel = new Map();
    	nextMapSameLevel.setLevel(level);
    	nextMapSameLevel.setOrder(2);
    	
    	Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

    	when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(nextMapSameLevel);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getOrder() + 1, entity.getOrder());
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getLevel().getOrder(), entity.getLevel().getOrder());
    }
    
    /*@Test
    public void findCurrentMap_WhenNextPhaseIsInTheNextLevel_returnFirstMapOfNextLevelWithFlagLevelCompletedChecked() {
    	// mock: this.findByGameAndLevel(game.getId(), lastPhaseCompleted.getPhase().getMap().getLevel().getId() + 1) precisa retornar o primeiro map do próximo level.
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	
    	Level level = new Level();
    	level.setOrder(1);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	map.setLevel(level);
    	
    	Phase phase = new Phase();
    	phase.setMap(map);
    	
    	lastPhaseCompleted.setPhase(phase);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals(1, entity.getOrder());
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getLevel().getOrder(), entity.getLevel().getOrder() + 1);
    	Assert.assertTrue(entity.isLevelCompleted());
    }
    
    @Test
    public void findCurrentMap_WhenItDoesntExistNextMap_returnSameMapOfTheLastPhaseCompletedWithFlagGameCompletedChecked() {
    	// mock: this.findByGameAndLevel(game.getId(), lastPhaseCompleted.getPhase().getMap().getLevel().getId() + 1) precisa retornar null.
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	
    	Level level = new Level();
    	level.setOrder(1);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	map.setLevel(level);
    	
    	Phase phase = new Phase();
    	phase.setMap(map);
    	
    	lastPhaseCompleted.setPhase(phase);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getOrder(), entity.getOrder());
    	Assert.assertTrue(entity.isGameCompleted());
    }
     findCurrentMap - end */
}