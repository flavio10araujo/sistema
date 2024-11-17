package com.polifono.service.game;

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
import com.polifono.model.entity.Content;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Question;
import com.polifono.repository.IGameRepository;
import com.polifono.service.QuestionService;

/**
 * Unit test methods for the GameService.
 */
@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameService service;

    @Mock
    private IGameRepository repository;

    @Mock
    private QuestionService questionService;

    /* findAll - begin */
    @Test
    public void findAll_WhenListAllGames_ReturnList() {
        List<Game> list = getEntityListStubData();

        when(repository.findAll()).thenReturn(list);

        List<Game> listReturned = service.findAll();
        Assertions.assertNotNull(listReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - not expected list size 0");

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }
    /* findAll - end */

    /* findByNamelink - begin */
    @Test
    public void findByNamelink_WhenSearchByNamelinkExistent_ReturnGame() {
        Optional<Game> entity = getEntityStubData();

        String NAME_LINK = "recorder";
        when(repository.findByNamelink(NAME_LINK)).thenReturn(entity.orElse(null));

        Optional<Game> entityReturned = service.findByNamelink(NAME_LINK);
        Assertions.assertTrue(entityReturned.isPresent(), "failure - expected not null for '" + NAME_LINK + "'");

        verify(repository, times(1)).findByNamelink(NAME_LINK);
        verifyNoMoreInteractions(repository);
    }
    /* findByNamelink - end */

    /* calculateScore - begin */
    @Test
    public void calculateScore_WhenFirstAttempt_ScoreReturnedIsTheSameThatTheGrade() {
        Assertions.assertEquals(100, service.calculateScore(1, 100));
        Assertions.assertEquals(90, service.calculateScore(1, 90));
        Assertions.assertEquals(80, service.calculateScore(1, 80));
        Assertions.assertEquals(70, service.calculateScore(1, 70));
    }

    @Test
    public void calculateScore_WhenSecondAttempt_ReturnScoreWithDiscount() {
        Assertions.assertEquals(70, service.calculateScore(2, 100));
        Assertions.assertEquals(65, service.calculateScore(2, 90));
        Assertions.assertEquals(60, service.calculateScore(2, 80));
        Assertions.assertEquals(50, service.calculateScore(2, 70));
    }

    @Test
    public void calculateScore_WhenThirdAttempt_ReturnScoreWithDiscount() {
        Assertions.assertEquals(50, service.calculateScore(3, 100));
        Assertions.assertEquals(45, service.calculateScore(3, 90));
        Assertions.assertEquals(40, service.calculateScore(3, 80));
        Assertions.assertEquals(30, service.calculateScore(3, 70));
    }

    @Test
    public void calculateScore_WhenMoreThanThreeAttempts_ReturnScoreAlways10() {
        Assertions.assertEquals(10, service.calculateScore(4, 100));
        Assertions.assertEquals(10, service.calculateScore(4, 90));
        Assertions.assertEquals(10, service.calculateScore(4, 80));
        Assertions.assertEquals(10, service.calculateScore(4, 70));
        Assertions.assertEquals(10, service.calculateScore(5, 100));
        Assertions.assertEquals(10, service.calculateScore(5, 90));
        Assertions.assertEquals(10, service.calculateScore(5, 80));
        Assertions.assertEquals(10, service.calculateScore(5, 70));
    }
    /* calculateScore - end */

    /* calculateGrade - begin */
    @Test
    public void givenAllAnswersAreRight_whenCalculateGrade_thenReturn100() {
        List<Integer> questionsId = List.of(101, 102, 103, 104, 105);
        Optional<Question> q1 = getQuestion(101);
        Optional<Question> q2 = getQuestion(102);
        Optional<Question> q3 = getQuestion(103);
        Optional<Question> q4 = getQuestion(104);
        Optional<Question> q5 = getQuestion(105);

        java.util.Map<String, String> playerAnswers = new java.util.HashMap<>();
        playerAnswers.put("101", "104");
        playerAnswers.put("102", "105");
        playerAnswers.put("103", "106");
        playerAnswers.put("104", "107");
        playerAnswers.put("105", "108");

        when(questionService.findById(101)).thenReturn(q1);
        when(questionService.findById(102)).thenReturn(q2);
        when(questionService.findById(103)).thenReturn(q3);
        when(questionService.findById(104)).thenReturn(q4);
        when(questionService.findById(105)).thenReturn(q5);

        int ret = service.calculateGrade(questionsId, playerAnswers);

        Assertions.assertEquals(100, ret);
    }

    @Test
    public void given80AnswersAreRight_whenCalculateGrade_thenReturn80() {
        List<Integer> questionsId = List.of(101, 102, 103, 104, 105);
        Optional<Question> q1 = getQuestion(101);
        Optional<Question> q2 = getQuestion(102);
        Optional<Question> q3 = getQuestion(103);
        Optional<Question> q4 = getQuestion(104);
        Optional<Question> q5 = getQuestion(105);

        java.util.Map<String, String> playerAnswers = new java.util.HashMap<>();
        playerAnswers.put("101", "104");
        playerAnswers.put("102", "105");
        playerAnswers.put("103", "106");
        playerAnswers.put("104", "999"); // wrong answer
        playerAnswers.put("105", "108");

        when(questionService.findById(101)).thenReturn(q1);
        when(questionService.findById(102)).thenReturn(q2);
        when(questionService.findById(103)).thenReturn(q3);
        when(questionService.findById(104)).thenReturn(q4);
        when(questionService.findById(105)).thenReturn(q5);

        int ret = service.calculateGrade(questionsId, playerAnswers);

        Assertions.assertEquals(80, ret);
    }

    @Test
    public void givenAllAnswersAreWrong_whenCalculateGrade_thenReturn0() {
        List<Integer> questionsId = List.of(101, 102, 103, 104, 105);
        Optional<Question> q1 = getQuestion(101);
        Optional<Question> q2 = getQuestion(102);
        Optional<Question> q3 = getQuestion(103);
        Optional<Question> q4 = getQuestion(104);
        Optional<Question> q5 = getQuestion(105);

        java.util.Map<String, String> playerAnswers = new java.util.HashMap<>();
        playerAnswers.put("101", "999");
        playerAnswers.put("102", "999");
        playerAnswers.put("103", "999");
        playerAnswers.put("104", "999");
        playerAnswers.put("105", "999");

        when(questionService.findById(101)).thenReturn(q1);
        when(questionService.findById(102)).thenReturn(q2);
        when(questionService.findById(103)).thenReturn(q3);
        when(questionService.findById(104)).thenReturn(q4);
        when(questionService.findById(105)).thenReturn(q5);

        int ret = service.calculateGrade(questionsId, playerAnswers);

        Assertions.assertEquals(0, ret);
    }

    @Test
    public void givenOneQuestionWasNotAnswered_whenCalculateGrade_thenReturn80() {
        List<Integer> questionsId = List.of(101, 102, 103, 104, 105);
        Optional<Question> q1 = getQuestion(101);
        Optional<Question> q2 = getQuestion(102);
        Optional<Question> q3 = getQuestion(103); // not answered
        Optional<Question> q4 = getQuestion(104);
        Optional<Question> q5 = getQuestion(105);

        java.util.Map<String, String> playerAnswers = new java.util.HashMap<>();
        playerAnswers.put("101", "104");
        playerAnswers.put("102", "105");
        playerAnswers.put("104", "107");
        playerAnswers.put("105", "108");

        when(questionService.findById(101)).thenReturn(q1);
        when(questionService.findById(102)).thenReturn(q2);
        when(questionService.findById(103)).thenReturn(q3);
        when(questionService.findById(104)).thenReturn(q4);
        when(questionService.findById(105)).thenReturn(q5);

        int ret = service.calculateGrade(questionsId, playerAnswers);

        Assertions.assertEquals(80, ret);
    }
    /* calculateGrade - end */

    /* getPhaseOfTheTest - begin */
    /* getPhaseOfTheTest - end */

    /* stubs - begin */
    public Optional<Game> getEntityStubData() {
        Game game = new Game();
        return Optional.of(game);
    }

    private List<Game> getEntityListStubData() {
        List<Game> list = new ArrayList<>();

        Game entity1 = getEntityStubData().orElse(null);
        Game entity2 = getEntityStubData().orElse(null);

        list.add(entity1);
        list.add(entity2);

        return list;
    }

    public Optional<Question> getQuestion(int id) {
        Question question = new Question();
        question.setId(id);
        question.setOrder(id);
        question.setName("Question " + id);
        question.setContent(getContent(id));
        question.setAnswers(getAnswers(id));
        return Optional.of(question);
    }

    public Content getContent(int id) {
        Content content = new Content();
        content.setPhase(getPhase(id));
        return content;
    }

    public Phase getPhase(int id) {
        Phase phase = new Phase();
        phase.setOrder(id);
        return phase;
    }

    public List<Answer> getAnswers(int id) {
        List<Answer> answers = new ArrayList<>();
        answers.add(Answer.builder().id(id + 1).name("Answer 1").right(false).build());
        answers.add(Answer.builder().id(id + 2).name("Answer 2").right(false).build());
        answers.add(Answer.builder().id(id + 3).name("Answer 3").right(true).build());
        answers.add(Answer.builder().id(id + 4).name("Answer 4").right(false).build());
        return answers;
    }
    /* stubs - end */
}
