package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Answer;
import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Question;
import com.polifono.repository.IGameRepository;
import com.polifono.service.IGameService;
import com.polifono.service.IQuestionService;

@Service
public class GameServiceImpl implements IGameService {

	private IGameRepository repository;
	
	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	public GameServiceImpl(IGameRepository repository) {
		this.repository = repository;
	}

	public final List<Game> findAll() {
		return (List<Game>) repository.findAll();
	}
	
	public final Game findByNamelink(String namelink) {
		return repository.findByNamelink(namelink);
	}
	
	/**
	 * This method is used to calculate the correct score to the player.
	 * 
	 * @param numAttempts
	 * @param grade
	 * @return
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
	 * 
	 * @param questionsId
	 * @param playerAnswers
	 * @return
	 */
	public int calculateGrade(List<Integer> questionsId, java.util.Map<String, String> playerAnswers) {
		Question questionAux = null;
		int countQuestionsRight = 0, phaseOrder = 0;
		String playerAnswer = null;
		
		// For each question submitted:
		for (Integer questionId : questionsId) {
			// Get the question from the database by your ID.
			questionAux = questionService.findOne(questionId);
			
			if (questionAux.getContent().getPhase().getOrder() > phaseOrder) {
				phaseOrder = questionAux.getContent().getPhase().getOrder();
			}
			
			playerAnswer = playerAnswers.get(questionId.toString());
			
			// If playerAnswer is null, it is because the player hasn't answered this question.
			if (playerAnswer != null) {
				for (Answer answer : questionAux.getAnswers()) {
					// If the player has chosen the right answer.
					if ((answer.getId() == Integer.parseInt(playerAnswer)) && answer.isRight()) {
						countQuestionsRight++;
						continue;
					}
				}
			}
		}
		
		return (countQuestionsRight * 100) / questionsId.size();
	}
	
	/**
	 * Get the phase of a test based on the ID of the last question of the test.
	 * 
	 * @param questionsId
	 * @return
	 */
	public final Phase getPhaseOfTheTest(List<Integer> questionsId) {
		Integer questionId = questionsId.get(questionsId.size() - 1);
		Question questionAux = questionService.findOne(questionId);
		return questionAux.getContent().getPhase();
	}
}