package com.polifono.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;

/**
 * 
 * Unit test methods for the MapService.
 * 
 */
@Transactional
public class MapServiceTest extends AbstractTest {

	@Autowired
    private IMapService service;
	
	private final Integer GAME_ID_EXISTENT = 1;
	private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;
	private final Integer LEVEL_ID_EXISTENT = 1;
	private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;
	private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin */
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
    	
    	Map entity = service.save(map);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger not 0", 0, entity.getId());
    }

    @Test
    public void save_updateMap() {
    	Integer id = new Integer(MAP_ID_EXISTENT);
        Map entity = service.findOne(id);

        // Changing all possible fields.
        // id - not possible to change.
        // game - it's possible to change, but it's hard to test because the system may have only one game.
        /*Game newGame = new Game();
        newGame.setId(GAME_ID_EXISTENT + 1);
        entity.setGame(newGame);*/
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
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_MapExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(MAP_ID_EXISTENT));
    }
    
    @Test
    public void delete_MapInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(MAP_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
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
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_ListIsNullOrEmpty_ExceptionThrown() {
    	List<Map> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findMapsByGame - begin */
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
    /* findMapsByGame - end */
    
    /* findMapsByGameAndLevel - begin */
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
    /* findMapsByGameAndLevel - end */
    
    /* findMapByGameAndLevel - begin */
    @Test
    public void findMapByGameAndLevel_SearchGameAndLevelExistents_ReturnItem() {
    	Map entity = service.findMapByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findMapByGameAndLevel_SearchGameAndLevelInexistents_ReturnNull() {
    	Map entity = service.findMapByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findMapByGameAndLevel - end */
    
    /* findMapByGameLevelAndOrder - begin */
    @Test
    public void findMapByGameLevelAndOrder_SearchGameLevelAndOrderExistents_ReturnItem() {
    	Map entity = service.findMapByGameLevelAndOrder(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT, 1);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findMapByGameLevelAndOrder_SearchGameLevelAndOrderInexistents_ReturnNull() {
    	Map entity = service.findMapByGameLevelAndOrder(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT, 0);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findMapByGameLevelAndOrder - end */
    
    /* findNextMapSameLevel - begin */
    @Test
    public void findNextMapSameLevel() {
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
    /* findNextMapSameLevel - end */
}