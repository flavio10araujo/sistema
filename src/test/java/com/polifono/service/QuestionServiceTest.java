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
import com.polifono.domain.Question;
import com.polifono.repository.IQuestionRepository;
import com.polifono.service.impl.QuestionServiceImpl;

/**
 * Unit test methods for the QuestionService.
 * 
 */
public class QuestionServiceTest extends AbstractTest {

    private IQuestionService service;
    
    @Mock
    private IQuestionRepository repository;
	
	private final Integer GAME_ID_EXISTENT = 1;
	private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer LEVEL_ID_EXISTENT = 1;
	private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;
	private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer PHASE_ID_EXISTENT = 1;
	private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer CONTENT_ID_EXISTENT = 1;
	private final Integer CONTENT_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer QUESTION_ID_EXISTENT = 1;
	private final Integer QUESTION_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new QuestionServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Question getEntityStubData() {
    	Content content = new Content();
    	content.setId(CONTENT_ID_EXISTENT);
    	
    	Question entity = new Question();
    	entity.setId(QUESTION_ID_EXISTENT);
    	entity.setContent(content);
    	entity.setName("QUESTION 01");
    	entity.setOrder(1);
    	
    	return entity;
    }
    
    private List<Question> getEntityListStubData() {
    	List<Question> list = new ArrayList<Question>();
    	
    	Question entity1 = getEntityStubData();
    	Question entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    /* stubs - end */
    
    /* save - begin */
    @Test
    public void save_WhenSaveQuestion_ReturnQuestionSaved() {
    	Question entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Question entityReturned = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", entityReturned);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), entityReturned.getId());
    	Assert.assertEquals("failure - expected content attribute match", entity.getContent().getId(), entityReturned.getContent().getId());
    	Assert.assertEquals("failure - expected name attribute match", entity.getName(), entityReturned.getName());
    	Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), entityReturned.getOrder());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenQuestionIsExistent_ReturnTrue() {
    	Question entity = getEntityStubData();
    	
    	when(repository.findOne(QUESTION_ID_EXISTENT)).thenReturn(entity);
    	doNothing().when(repository).delete(entity);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(QUESTION_ID_EXISTENT));
    	
    	verify(repository, times(1)).findOne(QUESTION_ID_EXISTENT);
    	verify(repository, times(1)).delete(entity);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void delete_WhenQuestionIsInexistent_ReturnFalse() {
    	when(repository.findOne(QUESTION_ID_INEXISTENT)).thenReturn(null);
    	
    	Assert.assertFalse("failure - expected return false", service.delete(QUESTION_ID_INEXISTENT));
    	
    	verify(repository, times(1)).findOne(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenQuestionIsExistent_ReturnQuestion() {
    	Question entity = getEntityStubData();
    	
    	when(repository.findOne(QUESTION_ID_EXISTENT)).thenReturn(entity);
    	
    	Question entityReturned = service.findOne(QUESTION_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", QUESTION_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(QUESTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenQuestionIsInexistent_ReturnNull() {
    	when(repository.findOne(QUESTION_ID_INEXISTENT)).thenReturn(null);
    	
    	Question entityReturned = service.findOne(QUESTION_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findOne(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllQuestions_ReturnList() {
    	List<Question> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<Question> listReturned = service.findAll();
    	
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */
    
    /* findByGame - begin */
    @Test
    public void findByGame_WhenSearchByGameExistent_ReturnList() {
    	List<Question> list = getEntityListStubData();
    	
    	when(repository.findByGame(GAME_ID_EXISTENT)).thenReturn(list);
    	
    	List<Question> listReturned = service.findByGame(GAME_ID_EXISTENT);
    	
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
    	when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> list = service.findByGame(GAME_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    	
    	verify(repository, times(1)).findByGame(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGame - end */
    
    /* findByGameAndLevel - begin */
    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
    	List<Question> list = getEntityListStubData();
    	
    	when(repository.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);
    	
    	List<Question> listReturned = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
    	when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
    	when(repository.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> listReturned = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
    	when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGameAndLevel - end */
    
    /* findByMap - begin */
    @Test
    public void findByMap_WhenSearchByMapExistent_ReturnList() {
    	List<Question> list = getEntityListStubData();
    	
    	when(repository.findByMap(MAP_ID_EXISTENT)).thenReturn(list);
    	
    	List<Question> listReturned = service.findByMap(MAP_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByMap_WhenSearchByMapInexistent_ReturnEmptyList() {
    	when(repository.findByMap(MAP_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> listReturned = service.findByMap(MAP_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByMap(MAP_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByMap - end */
    
    /* findByPhase - begin */
    @Test
    public void findByPhase_WhenSearchByPhaseExistent_ReturnList() {
    	List<Question> list = getEntityListStubData();
    	
    	when(repository.findByPhase(PHASE_ID_EXISTENT)).thenReturn(list);
    	
    	List<Question> listReturned = service.findByPhase(PHASE_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByPhase_WhenSearchByPhaseInexistent_ReturnEmptyList() {
    	when(repository.findByPhase(PHASE_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> listReturned = service.findByPhase(PHASE_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByPhase(PHASE_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByPhase - end */
    
    /* findByContent - begin */
    @Test
    public void findByContent_WhenSearchByContentExistent_ReturnList() {
    	List<Question> list = getEntityListStubData();
    	
    	when(repository.findByContent(CONTENT_ID_EXISTENT)).thenReturn(list);
    	
    	List<Question> listReturned = service.findByContent(CONTENT_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByContent(CONTENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByContent_WhenSearchByContentInexistent_ReturnEmptyList() {
    	when(repository.findByContent(CONTENT_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());
    	
    	List<Question> listReturned = service.findByContent(CONTENT_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByContent(CONTENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByContent - end */
}