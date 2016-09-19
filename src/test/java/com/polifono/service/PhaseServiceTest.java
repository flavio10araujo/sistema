package com.polifono.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;

/**
 * Unit test methods for the PhaseService.
 * 
 */
@Transactional
public class PhaseServiceTest extends AbstractTest {

	@Autowired
    private IPhaseService service;
	
	private final Integer PHASE_ID_EXISTENT = 1;
	private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;

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
    public void save_createPhase() {
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	Phase phase = new Phase();
    	phase.setName("Phase Test");
    	phase.setOrder(1);
    	phase.setMap(map);
    	
    	Phase entity = service.save(phase);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger not 0", 0, entity.getId());
    }

    @Test
    public void save_updatePhase() {
    	Integer id = new Integer(PHASE_ID_EXISTENT);
        Phase entity = service.findOne(id);

        // Changing all possible fields.
        // id - not possible to change.
        // map - it's possible to change...
        /*Map newMap = new Map();
        newMap.setId(MAP_ID_EXISTENT + 1);
        entity.setMap(newMap);*/
        entity.setName("Name Changed");
        entity.setOrder(entity.getOrder() + 1);

        // Saving the changes.
        Phase changedEntity = service.save(entity);

        Assert.assertNotNull("failure - expected not null", changedEntity);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), changedEntity.getId());
        
        // Get the entity in the database with the changes.
        Phase updatedEntity = service.findOne(changedEntity.getId());
        
        Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
        Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
        Assert.assertEquals("failure - expected game attribute match", entity.getMap().getId(), updatedEntity.getMap().getId());
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_PhaseExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(PHASE_ID_EXISTENT));
    	Phase entity = service.findOne(PHASE_ID_EXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void delete_PhaseInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(PHASE_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_PhaseExistentButReturnNull_ExceptionThrown() {
        Integer id = new Integer(PHASE_ID_EXISTENT);
        Phase entity = service.findOne(id);
        Assert.assertNotNull("failure - expected not null", entity);
    }
    
    @Test
    public void findOne_PhaseExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(PHASE_ID_EXISTENT);
        Phase entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    @Test
    public void findOne_PhaseInexistent_ReturnNull() {
        Integer id = PHASE_ID_INEXISTENT;
    	Phase entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_ListIsNull_ExceptionThrown() {
    	List<Phase> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    }

    @Test
    public void findAll_ListHasSizeZero_ExceptionThrown() {
    	List<Phase> list = service.findAll();
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findPhasesByGame - begin */
    /* findPhasesByGame - end */
    
    /* findPhasesByGameAndLevel - begin */
    /* findPhasesByGameAndLevel - end */
    
    /* findPhasesByMap - begin */
    /* findPhasesByMap - end */
    
    /* findByMapAndOrder - begin */
    /* findByMapAndOrder - end */
    
    /* findNextPhaseInThisMap - begin */
    /* findNextPhaseInThisMap - end */
    
    /* findLastPhaseDoneByPlayerAndGame - begin */
    /* findLastPhaseDoneByPlayerAndGame - end */
    
    /* findLastPhaseOfTheLevel - begin */
    /* findLastPhaseOfTheLevel - end */
    
    /* findGamesForProfile - begin */
    /* findGamesForProfile - end */   
}