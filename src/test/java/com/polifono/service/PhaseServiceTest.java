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
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.repository.IPhaseRepository;
import com.polifono.service.impl.PhaseServiceImpl;

/**
 * Unit test methods for the PhaseService.
 * 
 */
@Transactional
public class PhaseServiceTest extends AbstractTest {

    private IPhaseService service;
	
	@Mock
	private IPhaseRepository repository;
	
	private final Integer PHASE_ID_EXISTENT = 1;
	private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer GAME_ID_EXISTENT = 1;
	private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer LEVEL_ID_EXISTENT = 1;
	private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;
	private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer ORDER_EXISTENT = 1;
	private final Integer ORDER_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer PLAYER_ID_EXISTENT = 1;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
		service = new PhaseServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin */
    @Test
    public void save_WhenSavePhase_ReturnPhaseSaved() {
    	Integer id = new Integer(PHASE_ID_EXISTENT);
    	
    	Phase phaseSaved = new Phase();
    	Map mapPhaseSaved = new Map();
    	mapPhaseSaved.setId(MAP_ID_EXISTENT);
    	phaseSaved.setMap(mapPhaseSaved);
    	phaseSaved.setName("phase test");
    	phaseSaved.setOrder(20);
    	phaseSaved.setId(id);
    	
    	when(repository.findOne(id)).thenReturn(phaseSaved);
    	
        Phase entity = service.findOne(id);

        // Changing all possible fields.
        // id - not possible to change.
        Map newMap = new Map();
        newMap.setId(entity.getMap().getId() + 1);
        entity.setMap(newMap);
        entity.setName(entity.getName() + " Changed");
        entity.setOrder(entity.getOrder() + 1);

        when(repository.save(entity)).thenReturn(entity);
        
        // Saving the changes.
        Phase changedEntity = service.save(entity);

        Assert.assertNotNull("failure - expected not null", changedEntity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), changedEntity.getId());
        
        when(repository.findOne(changedEntity.getId())).thenReturn(changedEntity);
        
        // Get the entity in the database with the changes.
        Phase updatedEntity = service.findOne(changedEntity.getId());
        
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
        Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
        Assert.assertEquals("failure - expected game attribute match", entity.getMap().getId(), updatedEntity.getMap().getId());
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenPhaseIsExistent_ReturnTrue() {
    	Phase temp = new Phase();
    	
    	when(repository.findOne(PHASE_ID_EXISTENT)).thenReturn(temp);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(PHASE_ID_EXISTENT));
    	
    	when(repository.findOne(PHASE_ID_EXISTENT)).thenReturn(null);
    	
    	Phase entity = service.findOne(PHASE_ID_EXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void delete_WhenPhaseIsInexistent_ReturnFalse() {
    	when(repository.findOne(PHASE_ID_INEXISTENT)).thenReturn(null);
    	Assert.assertFalse("failure - expected return false", service.delete(PHASE_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenPhaseIsExistent_ReturnPhase() {
        Integer id = new Integer(PHASE_ID_EXISTENT);
        
        Phase phase = new Phase();
        phase.setId(id);
        when(repository.findOne(id)).thenReturn(phase);
        
        Phase entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }
    
    @Test
    public void findOne_WhenPhaseIsInexistent_ReturnNull() {
        Integer id = PHASE_ID_INEXISTENT;
        
        when(repository.findOne(id)).thenReturn(null);
        
    	Phase entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllPhases_ReturnList() {
    	List<Phase> listReturned = new ArrayList<Phase>();
    	listReturned.add(new Phase());
    	when(repository.findAll()).thenReturn(listReturned);
    	
    	List<Phase> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findPhasesByGame - begin */
    @Test
    public void findPhasesByGame_WhenSearchByGameExistent_ReturnList() {
    	int gameId = GAME_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	listReturned.add(new Phase());
    	when(repository.findPhasesByGame(gameId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByGame(GAME_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findPhasesByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
    	int gameId = GAME_ID_INEXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	when(repository.findPhasesByGame(gameId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByGame(GAME_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findPhasesByGame - end */
    
    /* findPhasesByGameAndLevel - begin */
    @Test
    public void findPhasesByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
    	int gameId = GAME_ID_EXISTENT, levelId = LEVEL_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	listReturned.add(new Phase());
    	when(repository.findPhasesByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findPhasesByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
    	int gameId = GAME_ID_INEXISTENT, levelId = LEVEL_ID_INEXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	when(repository.findPhasesByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findPhasesByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
    	int gameId = GAME_ID_EXISTENT, levelId = LEVEL_ID_INEXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	when(repository.findPhasesByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findPhasesByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
    	int gameId = GAME_ID_INEXISTENT, levelId = LEVEL_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	when(repository.findPhasesByGameAndLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findPhasesByGameAndLevel - end */
    
    /* findPhasesByMap - begin */
    @Test
    public void findPhasesByMap_WhenSearchByMapExistent_ReturnList() {
    	int mapId = MAP_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	listReturned.add(new Phase());
    	when(repository.findPhasesByMap(mapId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByMap(MAP_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findPhasesByMap_WhenSearchByMapInexistent_ReturnEmptyList() {
    	int mapId = MAP_ID_INEXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	when(repository.findPhasesByMap(mapId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByMap(MAP_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findPhasesByMap - end */
    
    /* findPhasesCheckedByMap - begin */
    @Test
    public void findPhasesCheckedByMap_WhenThereAreNotPhasesInTheMap_ReturnNull() {
    	int mapId = MAP_ID_EXISTENT; 
    	when(repository.findPhasesByMap(mapId)).thenReturn(null);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	
    	List<Phase> list = service.findPhasesCheckedByMap(map, lastPhaseCompleted);
    	Assert.assertNull(list);
    }
    
    @Test
    public void findPhasesCheckedByMap_WhenThePlayerHasNeverCompletedAnyPhaseOfThisGame_ReturnListOfPhasesWithTheFirstPhaseOpened() {
    	int mapId = MAP_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	listReturned.add(new Phase());
    	when(repository.findPhasesByMap(mapId)).thenReturn(listReturned);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = null;
    	
    	List<Phase> list = service.findPhasesCheckedByMap(map, lastPhaseCompleted);
    	Phase firstPhase = list.get(0);
    	Assert.assertTrue(firstPhase.isOpened());
    }
    
    @Test
    public void findPhasesCheckedByMap_WhenThePlayerHasAlreadyCompletedAtLeastOnePhaseOfTheGame_ReturnListOfPhaseWithAllPhasesOpenedUntilTheNextPhase() {
    	int mapId = MAP_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase p1 = new Phase();
    	p1.setOrder(1);
    	Phase p2 = new Phase();
    	p2.setOrder(2);
    	Phase p3 = new Phase();
    	p3.setOrder(3);
    	Phase p4 = new Phase();
    	p4.setOrder(4);
    	listReturned.add(p1);
    	listReturned.add(p2);
    	listReturned.add(p3);
    	listReturned.add(p4);
    	when(repository.findPhasesByMap(mapId)).thenReturn(listReturned);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	PlayerPhase lastPhaseCompleted = new PlayerPhase();
    	Phase phaseCompleted = new Phase();
    	phaseCompleted.setOrder(2);
    	lastPhaseCompleted.setPhase(phaseCompleted);
    	
    	List<Phase> list = service.findPhasesCheckedByMap(map, lastPhaseCompleted);
    	
    	Assert.assertTrue(list.get(0).isOpened());
    	Assert.assertTrue(list.get(1).isOpened());
    	Assert.assertTrue(list.get(2).isOpened());
    	Assert.assertFalse(list.get(3).isOpened());
    }
    /* findPhasesCheckedByMap - end */
    
    /* findByMapAndOrder - begin */
    @Test
    public void findByMapAndOrder_WhenSearchByMapAndOrderExistents_ReturnList() {
    	int mapId = MAP_ID_EXISTENT, phaseOrder = ORDER_EXISTENT;
    	Phase entityReturned = new Phase();
    	when(repository.findPhaseByMapAndOrder(mapId, phaseOrder)).thenReturn(entityReturned);
    	
    	Phase entity = service.findByMapAndOrder(MAP_ID_EXISTENT, ORDER_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", entity);
    }

    @Test
    public void findByMapAndOrder_WhenSearchByMapAndOrderInexistents_ReturnEmptyList() {
    	int mapId = MAP_ID_INEXISTENT, phaseOrder = ORDER_INEXISTENT;
    	when(repository.findPhaseByMapAndOrder(mapId, phaseOrder)).thenReturn(null);
    	
    	Phase entity = service.findByMapAndOrder(MAP_ID_INEXISTENT, ORDER_INEXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findByMapAndOrder - end */
    
    /* findNextPhaseInThisMap - begin */
    @Test
    public void findNextPhaseInThisMap_WhenNextPhaseInThisMapIsExistent_ReturnNextPhase() {
    	int mapId = MAP_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase item = new Phase();
    	item.setOrder(10);
    	Map map = new Map();
    	map.setId(3);
    	item.setMap(map);
    	listReturned.add(item);
    	when(repository.findPhasesByMap(mapId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByMap(MAP_ID_EXISTENT);
    	Phase firstPhase = list.get(0);
    	
    	int phaseOrder = (firstPhase.getOrder() + 1);
    	Phase entityReturned = new Phase();
    	entityReturned.setOrder(phaseOrder);
    	
    	when(repository.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1)).thenReturn(entityReturned);
    	
    	Phase entity = service.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1);
    	
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertEquals(firstPhase.getOrder() + 1, entity.getOrder());
    }
    
    @Test
    public void findNextPhaseInThisMap_WhenNextPhaseInThisMapIsInexistent_ReturnNull() {
    	int mapId = MAP_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase item = new Phase();
    	item.setOrder(10);
    	Map map = new Map();
    	map.setId(3);
    	item.setMap(map);
    	listReturned.add(item);
    	when(repository.findPhasesByMap(mapId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findPhasesByMap(MAP_ID_EXISTENT);
    	
    	Phase firstPhase = list.get(list.size() - 1);
    	
    	int phaseOrder = (firstPhase.getOrder() + 1);
    	when(repository.findNextPhaseInThisMap(mapId, phaseOrder)).thenReturn(null);
    	
    	Phase entity = service.findNextPhaseInThisMap(firstPhase.getMap().getId(), firstPhase.getOrder() + 1);
    	
    	Assert.assertNull("failure - expected null", entity);
    }
    /* findNextPhaseInThisMap - end */
    
    /* findLastPhaseDoneByPlayerAndGame - begin */
    @Test
    public void findLastPhaseDoneByPlayerAndGame_WhenPlayerHasAlreadyFinishedAtLeastOnePhase_ReturnLastPhaseDone() {
    	int playerId = PLAYER_ID_EXISTENT, gameId = GAME_ID_EXISTENT;
    	
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase phase = new Phase();
    	phase.setId(123);
    	listReturned.add(phase);
    	when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);
    	
    	Phase entity = service.findLastPhaseDoneByPlayerAndGame(PLAYER_ID_EXISTENT, GAME_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    }
    /* findLastPhaseDoneByPlayerAndGame - end */
    
    /* findLastPhaseOfTheLevel - begin */
    @Test
    public void findLastPhaseOfTheLevel_WhenEveryThingIsOK_ReturnLastPhaseOfTheLevel() {
    	int gameId = GAME_ID_EXISTENT, levelId = LEVEL_ID_EXISTENT;
    	Phase itemReturned = new Phase();
    	itemReturned.setId(123);
    	itemReturned.setOrder(10);
    	Map mapItemReturned = new Map();
    	mapItemReturned.setId(2);
    	itemReturned.setMap(mapItemReturned);
    	List<Phase> listReturned = new ArrayList<Phase>();
    	listReturned.add(itemReturned);
    	when(repository.findLastPhaseOfTheLevel(gameId, levelId)).thenReturn(listReturned);
    	
    	Phase entity = service.findLastPhaseOfTheLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
    	// The only way to be sure that entity is the last phase of the level is if when we use findNextPhaseInThisMap the return is null. 
    	int mapId = entity.getMap().getId(), phaseOrder = (entity.getOrder() + 1);
    	when(repository.findNextPhaseInThisMap(mapId, phaseOrder)).thenReturn(null);
    	
    	Phase entityNull = service.findNextPhaseInThisMap(entity.getMap().getId(), entity.getOrder() + 1);
    	Assert.assertNull("failure - expected null", entityNull);
    }
    /* findLastPhaseOfTheLevel - end */
    
    /* findGamesForProfile - begin */
    @Test
    public void findGamesForProfile_WhenPlayerIsExistent_ReturnList() {
    	int playerId = PLAYER_ID_EXISTENT;
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase item = new Phase();
    	Game game = new Game();
    	game.setId(4);
    	Map map = new Map();
    	map.setGame(game);
    	item.setMap(map);
    	listReturned.add(item);
    	when(repository.findGamesForProfile(playerId)).thenReturn(listReturned);
    	
    	List<Phase> list = service.findGamesForProfile(PLAYER_ID_EXISTENT);
    	Assert.assertNotNull("failure - expected not null", list);
    }
    /* findGamesForProfile - end */
    
    /* playerCanAccessThisPhase - begin */
    @Test
    public void playerCanAccessThisPhase_WhenAccessingFirstPhase_ReturnTrue() {
    	Phase phase = new Phase();
    	phase.setOrder(1);
    	
    	Player user = new Player();
    	
    	Assert.assertTrue(service.playerCanAccessThisPhase(phase, user));
    }
    
    @Test
    public void playerCanAccessThisPhase_WhenThePlayerIsTryingToAccessAPhaseButHeHadNeverFinishedAPhaseOfThisGame_ReturnFalse() {
    	int playerId = 1, gameId = 1;
    	
    	Phase phase = new Phase();
    	phase.setOrder(20);
    	Game game = new Game();
    	game.setId(gameId);
    	Map map = new Map();
    	map.setGame(game);
    	phase.setMap(map);
    	
    	Player user = new Player();
    	user.setId(playerId);
    	
    	when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(null);
    	
    	Assert.assertFalse(service.playerCanAccessThisPhase(phase, user));
    }
    
    @Test
    public void playerCanAccessThisPhase_WhenThePlayerIsTryingToAccessAPhaseThatHeHadAlreadyDone_ReturnTrue() {
    	int playerId = 1, gameId = 1;
    	
    	Phase phase = new Phase();
    	phase.setOrder(20);
    	Game game = new Game();
    	game.setId(gameId);
    	Map map = new Map();
    	map.setGame(game);
    	phase.setMap(map);
    	
    	Player user = new Player();
    	user.setId(playerId);
    	
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase item = new Phase();
    	item.setOrder(25); // Last phase done.
    	listReturned.add(item);
    	
    	when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);
    	
    	Assert.assertTrue(service.playerCanAccessThisPhase(phase, user));
    }
    
    @Test
    public void playerCanAccessThisPhase_WhenThePlayerIsTryingToAccessTheNextPhaseInTheRightSequence_ReturnTrue() {
    	int playerId = 1, gameId = 1;
    	
    	Phase phase = new Phase();
    	phase.setOrder(20);
    	Game game = new Game();
    	game.setId(gameId);
    	Map map = new Map();
    	map.setGame(game);
    	phase.setMap(map);
    	
    	Player user = new Player();
    	user.setId(playerId);
    	
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase item = new Phase();
    	item.setOrder(19); // Last phase done.
    	listReturned.add(item);
    	
    	when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);
    	
    	Assert.assertTrue(service.playerCanAccessThisPhase(phase, user));
    }
    
    @Test
    public void playerCanAccessThisPhase_WhenThePlayerIsTryingToAccessAFuturePhaseButItIsNotTheNextPhase_ReturnFalse() {
    	int playerId = 1, gameId = 1;
    	
    	Phase phase = new Phase();
    	phase.setOrder(20);
    	Game game = new Game();
    	game.setId(gameId);
    	Map map = new Map();
    	map.setGame(game);
    	phase.setMap(map);
    	
    	Player user = new Player();
    	user.setId(playerId);
    	
    	List<Phase> listReturned = new ArrayList<Phase>();
    	Phase item = new Phase();
    	item.setOrder(15); // Last phase done.
    	listReturned.add(item);
    	
    	when(repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId)).thenReturn(listReturned);
    	
    	Assert.assertFalse(service.playerCanAccessThisPhase(phase, user));
    }
    /* playerCanAccessThisPhase - end */
}