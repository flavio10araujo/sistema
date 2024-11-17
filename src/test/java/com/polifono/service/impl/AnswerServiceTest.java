package com.polifono.service.impl;

import static org.mockito.ArgumentMatchers.any;
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

import com.polifono.model.entity.Answer;
import com.polifono.model.entity.Question;
import com.polifono.repository.IAnswerRepository;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @InjectMocks
    private AnswerService service;

    @Mock
    private IAnswerRepository repository;

    private static final Integer GAME_ID_EXISTENT = 1;
    private static final Integer GAME_ID_INEXISTENT = Integer.MAX_VALUE;

    private static final Integer LEVEL_ID_EXISTENT = 1;
    private static final Integer LEVEL_ID_INEXISTENT = Integer.MAX_VALUE;

    private static final Integer MAP_ID_EXISTENT = 1;
    private static final Integer MAP_ID_INEXISTENT = Integer.MAX_VALUE;

    private static final Integer PHASE_ID_EXISTENT = 1;
    private static final Integer PHASE_ID_INEXISTENT = Integer.MAX_VALUE;

    private static final Integer QUESTION_ID_EXISTENT = 1;
    private static final Integer QUESTION_ID_INEXISTENT = Integer.MAX_VALUE;

    private static final Integer ANSWER_ID_EXISTENT = 1;
    private static final Integer ANSWER_ID_INEXISTENT = Integer.MAX_VALUE;

    /* save - begin */
    @Test
    public void givenAnswer_whenSave_thenReturnAnswerSaved() {
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
    public void givenAnswer_whenDelete_thenReturnTrue() {
        Optional<Answer> entity = getEntityStubData();

        when(repository.findById(ANSWER_ID_EXISTENT)).thenReturn(entity);
        doNothing().when(repository).delete(entity.get());

        Assertions.assertTrue(service.delete(ANSWER_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).delete(entity.get());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenInexistentAnswer_whenDelete_thenReturnFalse() {
        when(repository.findById(ANSWER_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(ANSWER_ID_INEXISTENT), "failure - expected return false");

        verify(repository, times(0)).delete(any());
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */

    /* findById - begin */
    @Test
    public void givenAnswer_whenFindById_thenReturnAnswer() {
        Optional<Answer> entity = getEntityStubData();

        when(repository.findById(ANSWER_ID_EXISTENT)).thenReturn(entity);

        Optional<Answer> entityReturned = service.findById(ANSWER_ID_EXISTENT);

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(ANSWER_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(ANSWER_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenInexistentAnswer_whenFindById_thenReturnNull() {
        when(repository.findById(ANSWER_ID_INEXISTENT)).thenReturn(null);

        Optional<Answer> entityReturned = service.findById(ANSWER_ID_INEXISTENT);

        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(ANSWER_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findById - end */

    /* findAll - begin */
    @Test
    public void givenAnswers_whenFindAll_thenReturnList() {
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
    public void givenGame_whenFindAllByGameId_thenReturnList() {
        List<Answer> list = getEntityListStubData();

        when(repository.findAllByGameId(GAME_ID_EXISTENT)).thenReturn(list);

        List<Answer> listReturned = service.findAllByGameId(GAME_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByGameId(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenInexistentGame_whenFindAllByGameId_thenReturnEmptyList() {
        when(repository.findAllByGameId(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Answer> listReturned = service.findAllByGameId(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByGameId(GAME_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGame - end */

    /* findByGameAndLevel - begin */
    @Test
    public void givenGameAndLevel_whenFindAllByGameIdAndLevelId_thenReturnList() {
        List<Answer> list = getEntityListStubData();

        when(repository.findAllByGameIdAndLevelId(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT)).thenReturn(list);

        List<Answer> listReturned = service.findAllByGameIdAndLevelId(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByGameIdAndLevelId(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenInexistentGameAndLevel_whenFindAllByGameIdAndLevelId_thenReturnEmptyList() {
        when(repository.findAllByGameIdAndLevelId(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findAllByGameIdAndLevelId(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByGameIdAndLevelId(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenGameExistentButLevelInexistent_whenFindAllByGameIdAndLevelId_thenReturnEmptyList() {
        when(repository.findAllByGameIdAndLevelId(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findAllByGameIdAndLevelId(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByGameIdAndLevelId(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenLevelExistentButGameInexistent_whenFindAllByGameIdAndLevelId_thenReturnEmptyList() {
        when(repository.findAllByGameIdAndLevelId(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findAllByGameIdAndLevelId(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByGameIdAndLevelId(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByGameAndLevel - end */

    /* findByMap - begin */
    @Test
    public void givenMap_whenFindAllByMapId_thenReturnList() {
        List<Answer> list = getEntityListStubData();

        when(repository.findAllByMapId(MAP_ID_EXISTENT)).thenReturn(list);

        List<Answer> listReturned = service.findAllByMapId(MAP_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByMapId(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenMapInexistent_whenFindAllByMapId_thenReturnEmptyList() {
        when(repository.findAllByMapId(MAP_ID_INEXISTENT)).thenReturn(new ArrayList<Answer>());

        List<Answer> listReturned = service.findAllByMapId(MAP_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByMapId(MAP_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByMap - end */

    /* findByPhase - begin */
    @Test
    public void givenPhase_whenFindAllByPhaseId_thenReturnList() {
        List<Answer> list = getEntityListStubData();

        when(repository.findAllByPhaseId(PHASE_ID_EXISTENT)).thenReturn(list);

        List<Answer> listReturned = service.findAllByPhaseId(PHASE_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByPhaseId(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenPhaseInexistent_whenFindAllByPhaseId_thenReturnEmptyList() {
        when(repository.findAllByPhaseId(PHASE_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Answer> listReturned = service.findAllByPhaseId(PHASE_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByPhaseId(PHASE_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByPhase - end */

    /* findByQuestion - begin */
    @Test
    public void givenQuestion_whenFindAllByQuestionId_thenReturnList() {
        List<Answer> list = getEntityListStubData();

        when(repository.findAllByQuestionId(QUESTION_ID_EXISTENT)).thenReturn(list);

        List<Answer> listReturned = service.findAllByQuestionId(QUESTION_ID_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findAllByQuestionId(QUESTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenQuestionInexistent_whenFindAllByQuestionId_thenReturnEmptyList() {
        when(repository.findAllByQuestionId(QUESTION_ID_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Answer> list = service.findAllByQuestionId(QUESTION_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");

        verify(repository, times(1)).findAllByQuestionId(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByQuestion - end */

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
}
