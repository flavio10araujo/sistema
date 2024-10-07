package com.polifono.service.impl;

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

import com.polifono.domain.Content;
import com.polifono.domain.Question;
import com.polifono.repository.IQuestionRepository;

/**
 * Unit test methods for the QuestionService.
 */
@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {

    @InjectMocks
    private QuestionServiceImpl service;

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

    /* stubs - begin */
    private Optional<Question> getEntityStubData() {
        Content content = new Content();
        content.setId(CONTENT_ID_EXISTENT);

        Question entity = new Question();
        entity.setId(QUESTION_ID_EXISTENT);
        entity.setContent(content);
        entity.setName("QUESTION 01");
        entity.setOrder(1);

        return Optional.of(entity);
    }

    private List<Question> getEntityListStubData() {
        List<Question> list = new ArrayList<Question>();

        Question entity1 = getEntityStubData().get();
        Question entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */

    /* save - begin */
    @Test
    public void save_WhenSaveQuestion_ReturnQuestionSaved() {
        Optional<Question> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        Question entityReturned = service.save(entity.get());

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(entity.get().getId(), entityReturned.getId(), "failure - expected id attribute match");
        Assertions.assertEquals(entity.get().getContent().getId(), entityReturned.getContent().getId(), "failure - expected content attribute match");
        Assertions.assertEquals(entity.get().getName(), entityReturned.getName(), "failure - expected name attribute match");
        Assertions.assertEquals(entity.get().getOrder(), entityReturned.getOrder(), "failure - expected order attribute match");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* delete - begin */
    @Test
    public void delete_WhenQuestionIsExistent_ReturnTrue() {
        Optional<Question> entity = getEntityStubData();

        when(repository.findById(QUESTION_ID_EXISTENT)).thenReturn(entity);
        doNothing().when(repository).delete(entity.get());

        Assertions.assertTrue(service.delete(QUESTION_ID_EXISTENT), "failure - expected return true");

        verify(repository, times(1)).findById(QUESTION_ID_EXISTENT);
        verify(repository, times(1)).delete(entity.get());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_WhenQuestionIsInexistent_ReturnFalse() {
        when(repository.findById(QUESTION_ID_INEXISTENT)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(QUESTION_ID_INEXISTENT), "failure - expected return false");

        verify(repository, times(1)).findById(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* delete - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenQuestionIsExistent_ReturnQuestion() {
        Optional<Question> entity = getEntityStubData();

        when(repository.findById(QUESTION_ID_EXISTENT)).thenReturn(entity);

        Optional<Question> entityReturned = service.findById(QUESTION_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(QUESTION_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(QUESTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenQuestionIsInexistent_ReturnNull() {
        when(repository.findById(QUESTION_ID_INEXISTENT)).thenReturn(null);

        Optional<Question> entityReturned = service.findById(QUESTION_ID_INEXISTENT);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(QUESTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllQuestions_ReturnList() {
        List<Question> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Question> listReturned = service.findAll();

        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

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

        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByGame(GAME_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGame_WhenSearchByGameInexistent_ReturnEmptyList() {
        when(repository.findByGame(GAME_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> list = service.findByGame(GAME_ID_INEXISTENT);
        Assertions.assertEquals(0, list.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameAndLevelInexistents_ReturnEmptyList() {
        when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByGameExistentButLevelInexistent_ReturnEmptyList() {
        when(repository.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> listReturned = service.findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByGameAndLevel(GAME_ID_EXISTENT, LEVEL_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByGameAndLevel_WhenSearchByLevelExistentButGameInexistent_ReturnEmptyList() {
        when(repository.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> listReturned = service.findByGameAndLevel(GAME_ID_INEXISTENT, LEVEL_ID_EXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByMap(MAP_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByMap_WhenSearchByMapInexistent_ReturnEmptyList() {
        when(repository.findByMap(MAP_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> listReturned = service.findByMap(MAP_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByPhase(PHASE_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByPhase_WhenSearchByPhaseInexistent_ReturnEmptyList() {
        when(repository.findByPhase(PHASE_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> listReturned = service.findByPhase(PHASE_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

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
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByContent(CONTENT_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByContent_WhenSearchByContentInexistent_ReturnEmptyList() {
        when(repository.findByContent(CONTENT_ID_INEXISTENT)).thenReturn(new ArrayList<Question>());

        List<Question> listReturned = service.findByContent(CONTENT_ID_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByContent(CONTENT_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByContent - end */
}
