package com.polifono.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.Answer;
import com.polifono.domain.Question;
import com.polifono.repository.IAnswerRepository;
import com.polifono.service.impl.AnswerServiceImpl;

import static org.mockito.Mockito.*;

/**
 * Unit test methods for the AnswerService.
 * 
 */
public class AnswerServiceTest extends AbstractTest {
	
    private IAnswerService service;
    
    @Mock
    private IAnswerRepository repository;
	
	private final Integer GAME_ID_EXISTENT = 1;
	private final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer LEVEL_ID_EXISTENT = 1;
	private final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer MAP_ID_EXISTENT = 1;
	private final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer PHASE_ID_EXISTENT = 1;
	private final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer QUESTION_ID_EXISTENT = 1;
	private final Integer QUESTION_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final Integer ANSWER_ID_EXISTENT = 1;
	private final Integer ANSWER_ID_INEXISTENT = Integer.MAX_VALUE;

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new AnswerServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Answer getEntityStubData() {
    	Question question = new Question();
    	question.setId(QUESTION_ID_EXISTENT);
    	
    	Answer answer = new Answer();
    	answer.setQuestion(question);
    	answer.setId(ANSWER_ID_EXISTENT);
    	answer.setName("ANSWER 01");
    	answer.setOrder(1);
    	answer.setRight(true);
    	
    	return answer;
    }
    
    private List<Answer> getEntityListStubData() {
    	List<Answer> list = new ArrayList<Answer>();
    	
    	Answer entity1 = getEntityStubData();
    	entity1.setId(entity1.getId() + 1);
    	entity1.setName(entity1.getName() + " 1");
    	entity1.setOrder(entity1.getOrder() + 1);
    	
    	Answer entity2 = getEntityStubData();
    	entity1.setId(entity1.getId() + 2);
    	entity1.setName(entity1.getName() + " 2");
    	entity1.setOrder(entity1.getOrder() + 2);
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    /* stubs - end */

    /* save - begin */
    @Test
    public void save_WhenSaveAnswer_ReturnAnswerSaved() {
    	Answer entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Answer entitySaved = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", entitySaved);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), entitySaved.getId());
    	Assert.assertEquals("failure - expected question attribute match", entity.getQuestion().getId(), entitySaved.getQuestion().getId());
    	Assert.assertEquals("failure - expected name attribute match", entity.getName(), entitySaved.getName());
    	Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), entitySaved.getOrder());
    	Assert.assertEquals("failure - expected right attribute match", entity.isRight(), entitySaved.isRight());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_WhenAnswerIsExistent_ReturnTrue() {
    	Answer entity = getEntityStubData();
    	
    	when(repository.findOne(ANSWER_ID_EXISTENT)).thenReturn(entity);
    	doNothing().when(repository).delete(entity);
    	
    	Assert.assertTrue("failure - expected return true", service.delete(ANSWER_ID_EXISTENT));
    	
    	verify(repository, times(1)).delete(entity);
        //verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void delete_WhenAnswerIsInexistent_ReturnFalse() {
    	when(repository.findOne(ANSWER_ID_INEXISTENT)).thenReturn(null);
    	
    	Assert.assertFalse("failure - expected return false", service.delete(ANSWER_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenAnswerIsExistent_ReturnAnswer() {
    	Answer entity = getEntityStubData();
    	
    	when(repository.findOne(ANSWER_ID_EXISTENT)).thenReturn(entity);
    	
    	Answer entityReturned = service.findOne(ANSWER_ID_EXISTENT);
        
    	Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", ANSWER_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(ANSWER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenAnswerIsInexistent_ReturnNull() {
    	when(repository.findOne(ANSWER_ID_INEXISTENT)).thenReturn(null);
    	
    	Answer entityReturned = service.findOne(ANSWER_ID_INEXISTENT);
    	
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findOne(ANSWER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_WhenListAllAnswers_ReturnList() {
    	List<Answer> list = getEntityListStubData();
    	
    	when(repository.findAll()).thenReturn(list);
    	
    	List<Answer> listReturned = service.findAll();
    	Assert.assertNotNull("failure - expected not null", listReturned);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, listReturned.size());
    	
    	verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */
    
    /* findByGame - begin */
    @Test
    public void findByGame_WhenSearchByGameExistent_ReturnList() {
    	List<Answer> list = getEntityListStubData();
    	
    	when(repository.findByGame(GAME_ID_EXISTENT)).thenReturn(list);
    	
    	List<Answer> listReturned = service.findByGame(GAME_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
    	when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> listReturned = service.findByGame(GAME_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGame(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGame - end */
    
    /* findByGameAndLevel - begin */
    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelExistents_ReturnList() {
    	List<Answer> list = getEntityListStubData();
    	
    	when(repository.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);
    	
    	List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
    	when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
    	when(repository.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    
    @Test
    public void findByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
    	when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGameAndLevel - end */
    
    /* findByMap - begin */
    @Test
    public void findByMap_WhenSearchByMapExistent_ReturnList() {
    	List<Answer> list = getEntityListStubData();
    	
    	when(repository.findByMap(MAP_ID_EXISTENT)).thenReturn(list);
    	
    	List<Answer> listReturned = service.findByMap(MAP_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByMap_WhenSearchByMapInexistent_ReturnEmptyList() {
    	when(repository.findByMap(MAP_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> listReturned = service.findByMap(MAP_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByMap(MAP_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByMap - end */
    
    /* findByPhase - begin */
    @Test
    public void findByPhase_WhenSearchByPhaseExistent_ReturnList() {
    	List<Answer> list = getEntityListStubData();
    	
    	when(repository.findByPhase(PHASE_ID_EXISTENT)).thenReturn(list);
    	
    	List<Answer> listReturned = service.findByPhase(PHASE_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByPhase_WhenSearchByPhaseInexistent_ReturnEmptyList() {
    	when(repository.findByPhase(PHASE_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> listReturned = service.findByPhase(PHASE_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findByPhase(PHASE_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByPhase - end */
    
    /* findByQuestion - begin */
    @Test
    public void findByQuestion_WhenSearchByQuestionExistent_ReturnList() {
    	List<Answer> list = getEntityListStubData();
    	
    	when(repository.findByQuestion(QUESTION_ID_EXISTENT)).thenReturn(list);
    	
    	List<Answer> listReturned = service.findByQuestion(QUESTION_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findByQuestion(QUESTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByQuestion_WhenSearchByQuestionInexistent_ReturnEmptyList() {
    	when(repository.findByQuestion(QUESTION_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());
    	
    	List<Answer> list = service.findByQuestion(QUESTION_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    	
    	verify(repository, times(1)).findByQuestion(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByQuestion - end */
}