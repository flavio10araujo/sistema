package com.polifono.service.impl.game;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Question;
import com.polifono.repository.IGameRepository;
import com.polifono.service.IQuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameService {

    private final IGameRepository repository;
    private final IQuestionService questionService;

    public List<Game> findAll() {
        return repository.findAll();
    }

    public List<Game> findByActive(boolean active) {
        return repository.findByActive(active);
    }

    public Optional<Game> findByNamelink(String namelink) {
        return Optional.ofNullable(repository.findByNamelink(namelink));
    }

    /**
     * This method is used to calculate the correct score to the player.
     */
    public int calculateScore(int numAttempts, int grade) {
		/*
		 1
		 100 = 100
		 90 = 90
		 80 = 80
		 70 = 70
		 2
		 100 = 70
		 90 = 65
		 80 = 60
		 70 = 50
		 3
		 100 = 50
		 90 = 45
		 80 = 40
		 70 = 30
		 4...
		 10
		 */

        if (numAttempts == 1) {
            return grade;
        }

        if (numAttempts == 2) {
            if (grade > 90) {
                return 70;
            }
            if (grade > 80) {
                return 65;
            }
            if (grade > 70) {
                return 60;
            }
            if (grade == 70) {
                return 50;
            }
        }

        if (numAttempts == 3) {
            if (grade > 90) {
                return 50;
            }
            if (grade > 80) {
                return 45;
            }
            if (grade > 70) {
                return 40;
            }
            if (grade == 70) {
                return 30;
            }
        }

        return 10;
    }

    /**
     * Calculate the user's grade in a test.
     * questionsId is an array with the IDs of the questions answered.
     * playerAnswers are the answers of the player.
     * This method returns the percentage of right answers given by the player.
     */
    public int calculateGrade(List<Integer> questionsId, java.util.Map<String, String> playerAnswers) {
        long countQuestionsRight = questionsId.stream()
                .map(questionService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(question -> {
                    String playerAnswer = playerAnswers.get(String.valueOf(question.getId()));
                    return playerAnswer != null && question.getAnswers().stream()
                            .anyMatch(answer -> answer.getId() == Integer.parseInt(playerAnswer) && answer.isRight());
                })
                .count();

        return (int) ((countQuestionsRight * 100) / questionsId.size());
    }

    /**
     * Get the phase of a test based on the ID of the last question of the test.
     */
    public Phase getPhaseOfTheTest(List<Integer> questionsId) {
        Integer questionId = questionsId.get(questionsId.size() - 1);
        Question questionAux = questionService.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question not found"));
        return questionAux.getContent().getPhase();
    }
}
