package com.polifono.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.Content;
import com.polifono.domain.Contenttype;
import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.repository.IContentRepository;
import com.polifono.service.impl.ContentServiceImpl;

/**
 * Unit test methods for the ContentService.
 * 
 */
public class ContentServiceTest extends AbstractTest {

    private IContentService service;
	
    @Mock
    private IContentRepository repository;
	
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
    	MockitoAnnotations.initMocks(this);
    	service = new ContentServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Content getEntityStubData() {
    	Phase phase = new Phase();
    	phase.setId(PHASE_ID_EXISTENT);
    	
    	Contenttype contenttype = new Contenttype();
    	contenttype.setId(CONTENTTYPE_TEXT);
    	
    	Content content = new Content();
    	content.setId(CONTENT_ID_EXISTENT);
    	content.setPhase(phase);
    	content.setContenttype(contenttype);
    	content.setContent("CONTENT");
    	content.setOrder(1);
    	
    	return content;
    }
    
    private List<Content> getEntityListStubData() {
    	List<Content> list = new ArrayList<Content>();
    	
    	Content entity1 = getEntityStubData();
    	Content entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    
    private List<Content> getEntityListTypeTextStubData() {
    	List<Content> list = new ArrayList<Content>();
    	
    	Contenttype contenttype = new Contenttype();
    	contenttype.setId(CONTENTTYPE_TEXT);
    	
    	Phase phase = new Phase();
    	phase.setId(PHASE_ID_EXISTENT);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	Level level = new Level();
    	level.setId(LEVEL_ID_EXISTENT);
    	
    	map.setGame(game);
    	map.setLevel(level);
    	phase.setMap(map);
    			
    	Content entity1 = getEntityStubData();
    	entity1.setContenttype(contenttype);
    	entity1.setPhase(phase);
    	
    	Content entity2 = getEntityStubData();
    	entity2.setContenttype(contenttype);
    	entity2.setPhase(phase);
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    
    private List<Content> getEntityListTypeTestStubData() {
    	List<Content> list = new ArrayList<Content>();
    	
    	Contenttype contenttype = new Contenttype();
    	contenttype.setId(CONTENTTYPE_TEST);
    	
    	Phase phase = new Phase();
    	phase.setId(PHASE_ID_EXISTENT);
    	
    	Map map = new Map();
    	map.setId(MAP_ID_EXISTENT);
    	
    	Game game = new Game();
    	game.setId(GAME_ID_EXISTENT);
    	
    	Level level = new Level();
    	level.setId(LEVEL_ID_EXISTENT);
    	
    	map.setGame(game);
    	map.setLevel(level);
    	phase.setMap(map);
    			
    	Content entity1 = getEntityStubData();
    	entity1.setContenttype(contenttype);
    	entity1.setPhase(phase);
    	
    	Content entity2 = getEntityStubData();
    	entity2.setContenttype(contenttype);
    	entity2.setPhase(phase);
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    /* stubs - end */
    
    /* save - begin */
    @Test
    public void save_WhenSaveContent_ReturnContentSaved() {
    	Content entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Content entitySaved = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", entitySaved);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), entitySaved.getId());
    	Assert.assertEquals("failure - expected phase attribute match", entity.getPhase().getId(), entitySaved.getPhase().getId());
    	Assert.assertEquals("failure - expected content type attribute match", entity.getContenttype().getId(), entitySaved.getContenttype().getId());
    	Assert.assertEquals("failure - expected content attribute match", entity.getContent(), entitySaved.getContent());
    	Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), entitySaved.getOrder());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenContentIsExistent_ReturnTrue() {
    	Content entity = getEntityStubData();
    	
    	when(repository.findOne(CONTENT_ID_EXISTENT)).thenReturn(entity);
    	doNothing().when(repository).delete(entity);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(CONTENT_ID_EXISTENT));
    	
    	verify(repository, times(1)).findOne(CONTENT_ID_EXISTENT);
    	verify(repository, times(1)).delete(entity);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void delete_WhenContentIsInexistent_ReturnFalse() {
    	when(repository.findOne(CONTENT_ID_INEXISTENT)).thenReturn(null);
    	
    	Assert.assertFalse("failure - expected return false", service.delete(CONTENT_ID_INEXISTENT));
    	
    	verify(repository, times(1)).findOne(CONTENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenContentIsExistent_ReturnContent() {
    	Content entity = getEntityStubData();
    	
    	when(repository.findOne(CONTENT_ID_EXISTENT)).thenReturn(entity);
    	
    	Content entityReturned = service.findOne(CONTENT_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", CONTENT_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(CONTENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenContentIsInexistent_ReturnNull() {
    	when(repository.findOne(CONTENT_ID_INEXISTENT)).thenReturn(null);
    	
    	Content entity = service.findOne(CONTENT_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
        
        verify(repository, times(1)).findOne(CONTENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllContent_ReturnList() {
    	List<Content> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<Content> listReturned = service.findAll();
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */
    
    /* findAllText - begin */
    @Test
    public void findAllText_WhenListAllContentOfTypeText_ReturnList() {
    	List<Content> list = getEntityListTypeTextStubData();
    	
    	when(repository.findAllText()).thenReturn(list);
    	
    	List<Content> listReturned = service.findAllText();
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    	}
    	
    	verify(repository, times(1)).findAllText();
        verifyNoMoreInteractions(repository);
    }
    /* findAllText - end */
    
    /* findContentsTextByGame - begin */
    @Test
    public void findContentsTextByGame_WhenSearchByGameExistent_ReturnList() {
    	List<Content> list = getEntityListTypeTextStubData();
    	
    	when(repository.findContentsTextByGame(GAME_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTextByGame(GAME_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTextByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByGame - end */
    
    /* findContentsTextByGameAndLevel - begin */
    @Test
    public void findContentsTextByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
    	List<Content> list = getEntityListTypeTextStubData();
    	
    	when(repository.findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    		Assert.assertEquals("expected level attribute match", LEVEL_ID_EXISTENT.intValue(), entity.getPhase().getMap().getLevel().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTextByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByGameAndLevel - end */
    
    /* findContentsTextByMap - begin */
    @Test
    public void findContentsTextByMap_WhenSearchByMapExistent_ReturnList() {
    	List<Content> list = getEntityListTypeTextStubData();
    	
    	when(repository.findContentsTextByMap(MAP_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTextByMap(MAP_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected map attribute match", MAP_ID_EXISTENT.intValue(), entity.getPhase().getMap().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTextByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByMap - end */
    
    /* findContentsTextByPhase - begin */
    @Test
    public void findContentsTextByPhase_WhenSearchByPhaseExistent_ReturnList() {
    	List<Content> list = getEntityListTypeTextStubData();
    	
    	when(repository.findContentsTextByPhase(PHASE_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTextByPhase(PHASE_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEXT.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTextByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTextByPhase - end */
    
    /* findAllTest - begin */
    @Test 
    public void findAllTest_WhenListAllContentOfTypeTest_ReturnList() {
    	List<Content> list = getEntityListTypeTestStubData();
    	
    	when(repository.findAllTest()).thenReturn(list);
    	
    	List<Content> listReturned = service.findAllTest();
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    	}
    	
    	verify(repository, times(1)).findAllTest();
        verifyNoMoreInteractions(repository);
    }
    /* findAllTest - end */
    
    /* findContentsTestByGame - begin */
    @Test
    public void findContentsTestByGame_WhenSearchByGameExistent_ReturnList() {
    	List<Content> list = getEntityListTypeTestStubData();
    	
    	when(repository.findContentsTestByGame(GAME_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTestByGame(GAME_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTestByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByGame - end */
    
    /* findContentsTestByGameAndLevel - begin */
    @Test
    public void findContentsTestByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
    	List<Content> list = getEntityListTypeTestStubData();
    	
    	when(repository.findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected game attribute match", GAME_ID_EXISTENT.intValue(), entity.getPhase().getMap().getGame().getId());
    		Assert.assertEquals("expected level attribute match", LEVEL_ID_EXISTENT.intValue(), entity.getPhase().getMap().getLevel().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTestByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByGameAndLevel - end */
    
    /* findContentsTestByMap - begin */
    @Test
    public void findContentsTestByMap_WhenSearchByMapExistent_ReturnList() {
    	List<Content> list = getEntityListTypeTestStubData();
    	
    	when(repository.findContentsTestByMap(MAP_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTestByMap(MAP_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected map attribute match", MAP_ID_EXISTENT.intValue(), entity.getPhase().getMap().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTestByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByMap - end */
    
    /* findContentsTestByPhase - begin */
    @Test
    public void findContentsTestByPhase_WhenSearchByPhaseExistent_ReturnList() {
    	List<Content> list = getEntityListTypeTestStubData();
    	
    	when(repository.findContentsTestByPhase(PHASE_ID_EXISTENT)).thenReturn(list);
    	
    	List<Content> listReturned = service.findContentsTestByPhase(PHASE_ID_EXISTENT);
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	for (Content entity : listReturned) {
    		Assert.assertEquals("expected content type attribute match", CONTENTTYPE_TEST.intValue(), entity.getContenttype().getId());
    		Assert.assertEquals("expected phase attribute match", PHASE_ID_EXISTENT.intValue(), entity.getPhase().getId());
    	}
    	
    	verify(repository, times(1)).findContentsTestByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findContentsTestByPhase - end */
    
    /* findByPhaseAndOrder - begin */
    @Test
    public void findByPhaseAndOrder_WhenSearchByPhaseAndOrderExistents_ReturnList() {
    	Content entity = getEntityStubData();
    	
    	when(repository.findByPhaseAndOrder(PHASE_ID_EXISTENT, 1)).thenReturn(entity);
    	
    	Content entityReturned = service.findByPhaseAndOrder(PHASE_ID_EXISTENT, 1);
    	Assert.assertNotNull("failure - expected not null", entityReturned);
    	Assert.assertEquals("expected phase type attribute match", PHASE_ID_EXISTENT.intValue(), entityReturned.getPhase().getId());
    	Assert.assertEquals("expected order attribute match", 1, entityReturned.getOrder());
    	
    	verify(repository, times(1)).findByPhaseAndOrder(PHASE_ID_EXISTENT, 1);
        verifyNoMoreInteractions(repository);
    }
    /* findByPhaseAndOrder - end */
}