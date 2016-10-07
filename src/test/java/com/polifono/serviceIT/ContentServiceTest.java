package com.polifono.serviceIT;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Content;
import com.polifono.domain.Contenttype;
import com.polifono.domain.Phase;
import com.polifono.service.IContentService;

/**
 * Unit test methods for the ContentService.
 * 
 */
@Transactional
public class ContentServiceTest extends AbstractTest {

	@Autowired
    private IContentService service;
	
	private final Integer CONTENT_ID_EXISTENT = 2;
	private final Integer CONTENT_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer PHASE_ID_EXISTENT = 1;
	//private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer CONTENTTYPE_TEST = 1;
	private final Integer CONTENTTYPE_TEXT = 2;
	
	private final Integer GAME_ID_EXISTENT = 1;
	//private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer LEVEL_ID_EXISTENT = 1;
	//private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;
	//private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin */
    //@Test
    public void save() {
    	Phase phase = new Phase();
    	phase.setId(PHASE_ID_EXISTENT);
    	
    	Contenttype contenttype = new Contenttype();
    	contenttype.setId(CONTENTTYPE_TEXT);
    	
    	Content content = new Content();
    	content.setPhase(phase);
    	content.setContenttype(contenttype);
    	content.setContent("CONTENT");
    	content.setOrder(1);
    	
    	Content entitySaved = service.save(content);
    	
    	Assert.assertNotNull("failure - expected not null", entitySaved);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entitySaved.getId());
    	
    	Content entity = service.findOne(entitySaved.getId()); 

    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
    	// Changing all possible fields.
    	phase.setId(phase.getId() + 1);
    	entity.setPhase(phase);
    	entity.setContent(entity.getContent() + " CHANGED");
    	entity.setOrder(entity.getOrder() + 1);
    	
    	Content updatedEntity = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", updatedEntity);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), updatedEntity.getId());
    	Assert.assertEquals("failure - expected phase attribute match", entity.getPhase().getId(), updatedEntity.getPhase().getId());
    	Assert.assertEquals("failure - expected content type attribute match", entity.getContenttype().getId(), updatedEntity.getContenttype().getId());
    	Assert.assertEquals("failure - expected content attribute match", entity.getContent(), updatedEntity.getContent());
    	Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
    }
    /* save - end */
    
    /* delete - begin */
    //@Test
    public void delete_ContentExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(CONTENT_ID_EXISTENT));
    	Content entity = service.findOne(CONTENT_ID_EXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    
    //@Test
    public void delete_ContentInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(CONTENT_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    //@Test
    public void findOne_ContentExistentButReturnNull_ExceptionThrown() {
        Content entity = service.findOne(CONTENT_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
    }

    //@Test
    public void findOne_ContentExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(CONTENT_ID_EXISTENT);
        Content entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    //@Test
    public void findOne_ContentInexistent_ReturnNull() {
        Integer id = CONTENT_ID_INEXISTENT;
    	Content entity = service.findOne(id);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    //@Test
    public void findAll_ListIsNullOrEmpty_ExceptionThrown() {
    	List<Content> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findAllText - begin */
    //@Test
    public void findAllText() {
    	List<Content> list = service.findAllText();
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    	}
    }
    /* findAllText - end */
    
    /* findContentsTextByGame - begin */
    //@Test
    public void findContentsTextByGame() {
    	List<Content> list = service.findContentsTextByGame(GAME_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    	}
    }
    /* findContentsTextByGame - end */
    
    /* findContentsTextByGameAndLevel - begin */
    //@Test
    public void findContentsTextByGameAndLevel() {
    	List<Content> list = service.findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    		Assert.assertEquals("expected level attribute match", LEVEL_ID_EXISTENT.intValue(), entity.getPhase().getMap().getLevel().getId());
    	}
    }
    /* findContentsTextByGameAndLevel - end */
    
    /* findContentsTextByMap - begin */
    //@Test
    public void findContentsTextByMap() {
    	List<Content> list = service.findContentsTextByMap(MAP_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected map attribute match", MAP_ID_EXISTENT.intValue(), entity.getPhase().getMap().getId());
    	}
    }
    /* findContentsTextByMap - end */
    
    /* findContentsTextByPhase - begin */
    //@Test
    public void findContentsTextByPhase() {
    	List<Content> list = service.findContentsTextByPhase(PHASE_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	}
    }
    /* findContentsTextByPhase - end */
    
    /* findAllTest - begin */
    //@Test 
    public void findAllTest() {
    	List<Content> list = service.findAllTest();
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    	}
    }
    /* findAllTest - end */
    
    /* findContentsTestByGame - begin */
    //@Test
    public void findContentsTestByGame() {
    	List<Content> list = service.findContentsTestByGame(GAME_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    	}
    }
    /* findContentsTestByGame - end */
    
    /* findContentsTestByGameAndLevel - begin */
    //@Test
    public void findContentsTestByGameAndLevel() {
    	List<Content> list = service.findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    		Assert.assertEquals("expected level attribute match", LEVEL_ID_EXISTENT.intValue(), entity.getPhase().getMap().getLevel().getId());
    	}
    }
    /* findContentsTestByGameAndLevel - end */
    
    /* findContentsTestByMap - begin */
    //@Test
    public void findContentsTestByMap() {
    	List<Content> list = service.findContentsTestByMap(MAP_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected map attribute match", MAP_ID_EXISTENT.intValue(), entity.getPhase().getMap().getId());
    	}
    }
    /* findContentsTestByMap - end */
    
    /* findContentsTestByPhase - begin */
    //@Test
    public void findContentsTestByPhase() {
    	List<Content> list = service.findContentsTestByPhase(PHASE_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    	
    	for (Content entity : list) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	}
    }
    /* findContentsTestByPhase - end */
    
    /* findByPhaseAndOrder - begin */
    //@Test
    public void findByPhaseAndOrder() {
    	Content entity = service.findByPhaseAndOrder(PHASE_ID_EXISTENT, 1);
    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertEquals("expected phase type attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	Assert.assertEquals("expected order attribute match", 1, entity.getOrder());
    }
    /* findByPhaseAndOrder - end */
}