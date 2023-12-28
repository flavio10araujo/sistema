package com.polifono.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.domain.Answer;
import com.polifono.domain.Question;
import com.polifono.repository.IAnswerRepository;
import com.polifono.service.impl.AnswerServiceImpl;

/**
 * Unit test methods for the AnswerService.
 */
@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @InjectMocks
    private AnswerServiceImpl service;

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

    /* stubs - begin */
    private Optional<Answer> getEntityStubData() {
        Question question = new Question();
        question.setId(QUESTION_ID_EXISTENT);

        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setId(ANSWER_ID_EXISTENT);
        answer.setName("ANSWER 01");
        answer.setOrder(1);
        answer.setRight(true);

        return Optional.of(answer);
    }

    private List<Answer> getEntityListStubData() {
        List<Answer> list = new ArrayList<>();

        Answer entity1 = getEntityStubData().get();
        entity1.setId(entity1.getId() + 1);
        entity1.setName(entity1.getName() + " 1");
        entity1.setOrder(entity1.getOrder() + 1);

        Answer entity2 = getEntityStubData().get();
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
        Optional<Answer> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        Answer entitySaved = service.save(entity.get());

        Assertions.assertNotNull(entitySaved, "failure - expected not null");
        Assertions.assertEquals(entity.get().getId(), entitySaved.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.get().getQuestion().getId(), entitySaved.getQuestion().getId(), "failure - expected question attribute match");
        Assertions.assertEquals(entity.get().getName(), entitySaved.getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.get().getOrder(), entitySaved.getOrder(), "failure - expected order attribute match");
        Assertions.assertEquals(entity.get().isRight(), entitySaved.isRight(), "failure - expected right attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_WhenAnswerIsExistent_ReturnTrue() {
        Optional<Answer> entity = getEntityStubData();

        when(repository.findById(ANSWER_ID_EXISTENT)).thenReturn(entity);
        doNothing().when(repository).delete(entity.get());

        Assertions.assertTrue(service.delete(ANSWER_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).delete(entity.get());
        //verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenAnswerIsInexistent_ReturnFalse() {
        when(repository.findById(ANSWER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(ANSWER_ID_INEXISTENT), "failure - expected return false");
    }
    /* delete - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenAnswerIsExistent_ReturnAnswer() {
        Optional<Answer> entity = getEntityStubData();

        when(repository.findById(ANSWER_ID_EXISTENT)).thenReturn(entity);

        Optional<Answer> entityReturned = service.findById(ANSWER_ID_EXISTENT);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(ANSWER_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(ANSWER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenAnswerIsInexistent_ReturnNull() {
        when(repository.findById(ANSWER_ID_INEXISTENT)).thenReturn(null);

        Optional<Answer> entityReturned = service.findById(ANSWER_ID_INEXISTENT);

        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(ANSWER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllAnswers_ReturnList() {
        List<Answer> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Answer> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
        when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findByGame(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
        when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
        when(repository.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
        when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByMap_WhenSearchByMapInexistent_ReturnEmptyList() {
        when(repository.findByMap(MAP_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findByMap(MAP_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByPhase_WhenSearchByPhaseInexistent_ReturnEmptyList() {
        when(repository.findByPhase(PHASE_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findByPhase(PHASE_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByQuestion(QUESTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByQuestion_WhenSearchByQuestionInexistent_ReturnEmptyList() {
        when(repository.findByQuestion(QUESTION_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> list = service.findByQuestion(QUESTION_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");

        verify(repository, times(1)).findByQuestion(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByQuestion - end */
}
