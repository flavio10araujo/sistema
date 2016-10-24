package com.polifono.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
import com.polifono.repository.IMapRepository;
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
	private IMapRepository repository;
    
    @Mock
	private IPhaseService phaseService;
	
	private final Integer GAME_ID_EXISTENT = 1;
	
	private final Integer MAP_ID_EXISTENT = 1;

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
    
    /* save - begin */
    @Test
    public void save_WhenSaveMap_ReturnMapSaved() {
    	Integer id = 123;
    	
    	Map mapSaved = new Map();
    	mapSaved.setId(id);
    	mapSaved.setName("Map Test");
    	mapSaved.setOrder(3);
    	Game gameMapSaved = new Game();
    	gameMapSaved.setId(2);
        mapSaved.setGame(gameMapSaved);
        Level levelMapSaved = new Level();
        levelMapSaved.setId(3);
        mapSaved.setLevel(levelMapSaved);
    	
    	when(repository.findOne(id)).thenReturn(mapSaved);
    	
        Map entity = service.findOne(id);
        
        // Changing all possible fields.
        // id - not possible to change.
        Game newGame = new Game();
        newGame.setId(entity.getGame().getId() + 1);
        entity.setGame(newGame);
        Level newLevel = new Level();
        newLevel.setId(entity.getLevel().getId() + 1);
        entity.setLevel(newLevel);
        entity.setName(entity.getName() + " Changed");
        entity.setOrder(entity.getOrder() + 1);
        
        when(repository.save(entity)).thenReturn(entity);

        // Saving the changes.
        Map changedEntity = service.save(entity);
        
        Assert.assertNotNull("failure - expected not null", changedEntity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), changedEntity.getId());
        
        when(repository.findOne(changedEntity.getId())).thenReturn(changedEntity);
        
        // Get the entity in the database with the changes.
        Map updatedEntity = service.findOne(changedEntity.getId());
        
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
        Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
        Assert.assertEquals("failure - expected game attribute match", entity.getGame().getId(), updatedEntity.getGame().getId());
        Assert.assertEquals("failure - expected level attribute match", entity.getLevel().getId(), updatedEntity.getLevel().getId());
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenMapIsExistent_ReturnTrue() {
    	int id = 234;
    	Map temp = new Map();
    	
    	when(repository.findOne(id)).thenReturn(temp);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(id));
    	
    	when(repository.findOne(id)).thenReturn(null);
    	
    	Map entity = service.findOne(id);
    	Assert.assertNull("failure - expected null", entity);
    }

    @Test
    public void delete_WhenMapIsInexistent_ReturnFalse() {
    	int id = 999;
    	when(repository.findOne(id)).thenReturn(null);
    	Assert.assertFalse("failure - expected return false", service.delete(id));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenMapIsExistent_ReturnMap() {
        Integer id = 123;
        
        Map map = new Map();
        map.setId(123);
        when(repository.findOne(id)).thenReturn(map);
        
        Map entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }
    
    @Test
    public void findOne_WhenMapIsInexistent_ReturnNull() {
        Integer id = 999;
        
        when(repository.findOne(id)).thenReturn(null);
        
    	Map entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllMaps_ReturnList() {
    	List<Map> listReturned = new ArrayList<Map>();
    	listReturned.add(new Map());
    	when(repository.findAll()).thenReturn(listReturned);
    	
    	List<Map> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findMapsByGame - begin */
    @Test
    public void findMapsByGame_WhenSearchByGameExistent_ReturnList() {
    	int gameId = 1;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	listReturned.add(new Map());
    	when(repository.findMapsByGame(gameId)).thenReturn(listReturned);
    	
    	List<Map> list = service.findMapsByGame(gameId);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findMapsByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
    	int gameId = 999;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	when(repository.findMapsByGame(gameId)).thenReturn(listReturned);
    	
    	List<Map> list = service.findMapsByGame(gameId);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findMapsByGame - end */
    
    /* findMapsByGameAndLevel - begin */
    @Test
    public void findMapsByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
    	int gameId = 1, levelId = 1;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	listReturned.add(new Map());
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Map> list = service.findMapsByGameAndLevel(gameId, levelId);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findMapsByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
    	int gameId = 999, levelId = 999;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Map> list = service.findMapsByGameAndLevel(gameId, levelId);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findMapsByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
    	int gameId = 1, levelId = 999;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Map> list = service.findMapsByGameAndLevel(gameId, levelId);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findMapsByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
    	int gameId = 999, levelId = 1;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Map> list = service.findMapsByGameAndLevel(gameId, levelId);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findMapsByGameAndLevel - end */
    
    /* findByGameAndLevel - begin */
    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnMap() {
    	int gameId = 1, levelId = 1;
    	List<Map> listReturned = new ArrayList<Map>();
    	listReturned.add(new Map());
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	Map entity = service.findByGameAndLevel(gameId, levelId);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnNull() {
    	int gameId = 1, levelId = 1;
    	List<Map> listReturned = new ArrayList<Map>();
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	Map entity = service.findByGameAndLevel(gameId, levelId);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findByGameAndLevel - end */
    
    /* findByGameLevelAndOrder - begin */
    @Test
    public void findByGameLevelAndOrder_WhenSearchByGameLevelAndOrderExistents_ReturnMap() {
    	int gameId = 1, levelId = 1, mapOrder = 1;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	listReturned.add(new Map());
    	when(repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder)).thenReturn(listReturned);
    	
    	Map entity = service.findByGameLevelAndOrder(gameId, levelId, mapOrder);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findByGameLevelAndOrder_WhenSearchByGameLevelAndOrderInexistents_ReturnNull() {
    	int gameId = 999, levelId = 999, mapOrder = 999;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	
    	when(repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder)).thenReturn(listReturned);
    	
    	Map entity = service.findByGameLevelAndOrder(gameId, levelId, mapOrder);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findByGameLevelAndOrder - end */
    
    /* findNextMapSameLevel - begin */
    @Test
    public void findNextMapSameLevel_WhenNextMapExists_ReturnNextMap() {
    	Map map = new Map();
    	Game game = new Game();
    	game.setId(1);
    	map.setGame(game);
    	Level level = new Level();
    	level.setId(1);
    	map.setLevel(level);
    	
    	when(repository.findOne(MAP_ID_EXISTENT)).thenReturn(map);
    	
    	Map mapCurrent = service.findOne(MAP_ID_EXISTENT);
    	
    	Map nextMapSameLevel = new Map();
    	nextMapSameLevel.setGame(mapCurrent.getGame());
    	nextMapSameLevel.setLevel(mapCurrent.getLevel());
    	nextMapSameLevel.setName("Next Map Same Level");
    	nextMapSameLevel.setOrder(mapCurrent.getOrder() + 1);
    	nextMapSameLevel.setId(50);
    	
    	when(repository.save(nextMapSameLevel)).thenReturn(nextMapSameLevel);
    	nextMapSameLevel = service.save(nextMapSameLevel);
    	
    	when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(nextMapSameLevel);
    	
    	Map entity = service.findNextMapSameLevel(mapCurrent);
    	
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertEquals(nextMapSameLevel.getId(), entity.getId());
    }
    /* findNextMapSameLevel - end */
    
    /* playerCanAccessThisMap - begin */
    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessTheFirstMapOfTheFirstLevel_ReturnTrue() {
    	Level level = new Level();
    	level.setOrder(1);
    	
    	Map map = new Map(); // Map that the player is trying to access.
    	map.setOrder(1);
    	map.setLevel(level);
    	
    	Player player = new Player();
    	
    	Assert.assertTrue(service.playerCanAccessThisMap(map, player));
    }

    @Test
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapDifferentOfTheFirstMapOfTheFirstLevelAndThePlayerHasNeverFinishedAPhaseOfThisGame_ReturnFalse() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAMapInAPreviousLevelThanTheLastPhaseDonesLevel_ReturnTrue() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessAPreviousMapAtTheSameLevelOfTheLastPhaseDone_ReturnTrue() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapButTheLastPhaseDoneIsNotTheLastPhaseOfTheLevel_ReturnFalse() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelAndItIsTheNext_ReturnTrue() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsInTheSameLevelButItIsNotTheNext_ReturnFalse() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapIsTheFirstOfTheNextLevel_ReturnTrue() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessANextMapAndTheLastPhaseDoneIsTheLastPhaseOfTheLevelTheMapINotTheFirstOfTheNextLevel_ReturnFalse() {
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
    public void playerCanAccessThisMap_WhenThePlayerIsTryingToAccessMapButTheLevelOfThisMapIsNotTheNextLevel_ReturnFalse() {
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
    /* playerCanAccessThisMap - end */
    
    /* findCurrentMap - begin */
    @Test
    public void findCurrentMap_WhenPlayerHasNeverCompletedAnyPhaseOfTheGame_ReturnFirstMapOfTheFirstLevelOfTheGame() {
    	int gameId = 1, levelId = 1;
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	Map map = new Map();
    	map.setOrder(1);
    	Game gameMapReturned = new Game();
    	gameMapReturned.setId(GAME_ID_EXISTENT);
    	map.setGame(gameMapReturned);
    	Level levelMapReturned = new Level();
    	levelMapReturned.setOrder(1);
    	map.setLevel(levelMapReturned);
    	listReturned.add(map);
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = null;
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals("failure - expected map order attribute match", 1, entity.getOrder());
    	Assert.assertEquals("failure - expected level order attribute match", 1, entity.getLevel().getOrder());
    	Assert.assertEquals("failure - expected game id attribute match", game.getId(), entity.getGame().getId());
    }
    
    @Test
    public void findCurrentMap_WhenNextPhaseIsInTheSameMapThatTheLastPhaseCompleted_ReturnSameMapOfTheLastPhaseCompleted() {
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
    public void findCurrentMap_WhenNextPhaseIsInTheNextMapInTheSameLevel_ReturnMapWithNextOrderAndInTheSameLevel() {
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	Level level = new Level();
    	level.setOrder(1);
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	map.setOrder(1);
    	map.setLevel(level);
    	map.setGame(game);
    	Phase phase = new Phase();
    	phase.setMap(map);
    	lastPhaseCompleted.setPhase(phase);

    	Map nextMapSameLevel = new Map();
    	nextMapSameLevel.setLevel(level);
    	nextMapSameLevel.setOrder(2);
    	
    	Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

    	when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(null);
    	when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(nextMapSameLevel);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getOrder() + 1, entity.getOrder());
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getLevel().getOrder(), entity.getLevel().getOrder());
    }
    
    @Test
    public void findCurrentMap_WhenNextPhaseIsInTheNextLevel_ReturnFirstMapOfNextLevelWithFlagLevelCompletedChecked() {
    	int gameId = 1, levelId = 1;
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	Level level = new Level();
    	level.setOrder(1);
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	map.setOrder(1);
    	map.setLevel(level);
    	map.setGame(game);
    	Phase phase = new Phase();
    	phase.setMap(map);
    	lastPhaseCompleted.setPhase(phase);

    	Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

    	when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(null);
    	when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(null);
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	Map mapReturned = new Map();
    	mapReturned.setOrder(1);
    	mapReturned.setLevelCompleted(true);
    	Level levelReturned = new Level();
    	levelReturned.setOrder(2);
    	mapReturned.setLevel(levelReturned);
    	
    	listReturned.add(mapReturned);
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals(1, entity.getOrder());
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getLevel().getOrder() + 1, entity.getLevel().getOrder());
    	Assert.assertTrue(entity.isLevelCompleted());
    }
    
    @Test
    public void findCurrentMap_WhenItDoesntExistNextMap_ReturnSameMapOfTheLastPhaseCompletedWithFlagGameCompletedChecked() {
    	int gameId = 1, levelId = 1;
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	Level level = new Level();
    	level.setOrder(1);
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	map.setOrder(1);
    	map.setLevel(level);
    	map.setGame(game);
    	Phase phase = new Phase();
    	phase.setMap(map);
    	lastPhaseCompleted.setPhase(phase);

    	Map mapCurrent = lastPhaseCompleted.getPhase().getMap();

    	when(phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1)).thenReturn(null);
    	when(repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder())).thenReturn(null);
    	
    	List<Map> listReturned = new ArrayList<Map>();
    	
    	when(repository.findMapsByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	Map entity = service.findCurrentMap(game, lastPhaseCompleted);
    	
    	Assert.assertEquals(lastPhaseCompleted.getPhase().getMap().getOrder(), entity.getOrder());
    	Assert.assertTrue(entity.isGameCompleted());
    }
    /* findCurrentMap - end */
}