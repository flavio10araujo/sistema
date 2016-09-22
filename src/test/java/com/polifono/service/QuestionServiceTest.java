package com.polifono.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Content;
import com.polifono.domain.Question;

/**
 * Unit test methods for the QuestionService.
 * 
 */
@Transactional
public class QuestionServiceTest extends AbstractTest {

	@Autowired
    private IQuestionService service;
	
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
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin */
    @Test
    public void save() {
    	Content content = new Content();
    	content.setId(CONTENT_ID_EXISTENT);
    	
    	Question question = new Question();
    	question.setContent(content);
    	question.setName("QUESTION 01");
    	question.setOrder(1);
    	
    	Question entitySaved = service.save(question);
    	
    	Assert.assertNotNull("failure - expected not null", entitySaved);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entitySaved.getId());
    	
    	Question entity = service.findOne(entitySaved.getId()); 

    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
    	// Changing all possible fields.
    	//content.setId(content.getId() + 1);
    	entity.setContent(content);
    	entity.setName(entity.getName() + " CHANGED");
    	entity.setOrder(entity.getOrder() + 1);
    	
    	Question updatedEntity = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", updatedEntity);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), updatedEntity.getId());
    	Assert.assertEquals("failure - expected content attribute match", entity.getContent().getId(), updatedEntity.getContent().getId());
    	Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
    	Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
    }
    /* save - end */
    
    /* delete - begin */
    @Test
    public void delete_QuestionExistent_ReturnTrue() {
    	Assert.assertTrue("failure - expected return true", service.delete(QUESTION_ID_EXISTENT));
    	Question entity = service.findOne(QUESTION_ID_EXISTENT);
    	Assert.assertNull("failure - expected null", entity);
    }
    
    @Test
    public void delete_QuestionInexistent_ReturnFalse() {
    	Assert.assertFalse("failure - expected return false", service.delete(QUESTION_ID_INEXISTENT));
    }
    /* delete - end */
    
    /* findOne - begin */
    @Test
    public void findOne_QuestionExistentButReturnNull_ExceptionThrown() {
        Question entity = service.findOne(QUESTION_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
    }

    @Test
    public void findOne_QuestionExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(QUESTION_ID_EXISTENT);
        Question entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    @Test
    public void findOne_QuestionInexistent_ReturnNull() {
        Question entity = service.findOne(QUESTION_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findAll - begin */
    @Test
    public void findAll_ListIsNullOrEmpty_ExceptionThrown() {
    	List<Question> list = service.findAll();
    	Assert.assertNotNull("failure - expected not null", list);
    	Assert.assertNotEquals("failure - not expected list size 0", 0, list.size());
    }
    /* findAll - end */
    
    /* findQuestionsByGame - begin */
    @Test
    public void findQuestionsByGame_SearchGameExistent_ReturnList() {
    	List<Question> list = service.findQuestionsByGame(GAME_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findQuestionsByGame_SearchGameInexistent_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByGame(GAME_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findQuestionsByGame - end */
    
    /* findQuestionsByGameAndLevel - begin */
    @Test
    public void findQuestionsByGameAndLevel_SearchGameAndLevelExistents_ReturnList() {
    	List<Question> list = service.findQuestionsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findQuestionsByGameAndLevel_SearchGameAndLevelInexistents_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findQuestionsByGameAndLevel_SearchGameExistentButLevelInexistent_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    
    @Test
    public void findQuestionsByGameAndLevel_SearchLevelExistentButGaemInexistent_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findQuestionsByGameAndLevel - end */
    
    /* findQuestionsByMap - begin */
    @Test
    public void findQuestionsByMap_SearchMapExistent_ReturnList() {
    	List<Question> list = service.findQuestionsByMap(MAP_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findQuestionsByMap_SearchMapInexistent_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByMap(MAP_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findQuestionsByMap - end */
    
    /* findQuestionsByPhase - begin */
    @Test
    public void findQuestionsByPhase_SearchPhaseExistent_ReturnList() {
    	List<Question> list = service.findQuestionsByPhase(PHASE_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findQuestionsByPhase_SearchPhaseInexistent_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByPhase(PHASE_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findQuestionsByPhase - end */
    
    /* findQuestionsByContent - begin */
    @Test
    public void findQuestionsByContent_SearchContentExistent_ReturnList() {
    	List<Question> list = service.findQuestionsByContent(CONTENT_ID_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findQuestionsByContent_SearchContentInexistent_ReturnListEmpty() {
    	List<Question> list = service.findQuestionsByContent(CONTENT_ID_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findQuestionsByContent - end */
}