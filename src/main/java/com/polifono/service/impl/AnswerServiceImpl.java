package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Answer;
import com.polifono.repository.AnswerRepository;

@Service
public class AnswerServiceImpl {

	@Autowired
	private AnswerRepository answerRepository;
	
	public final Answer save(Answer answer) {
		return answerRepository.save(answer);
	}
	
	public Boolean delete(Integer id) {
		Answer temp = answerRepository.findOne(id);
		
		if (temp != null) {
			try {
				answerRepository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final Answer find(int answerId) {
		return answerRepository.findOne(answerId);
	}
	
	public final List<Answer> findAll() {
		return (List<Answer>) answerRepository.findAll();
	}
	
	public final List<Answer> findAnswersByGame(int gameId) {
		return answerRepository.findAnswersByGame(gameId);
	}
	
	public final List<Answer> findAnswersByGameAndLevel(int gameId, int levelId) {
		return answerRepository.findAnswersByGameAndLevel(gameId, levelId);
	}
	
	public final List<Answer> findAnswersByMap(int mapId) {
		return answerRepository.findAnswersByMap(mapId);
	}
	
	public final List<Answer> findAnswersByPhase(int phaseId) {
		return answerRepository.findAnswersByPhase(phaseId);
	}
	
	public final List<Answer> findAnswersByQuestion(int questionId) {
		return answerRepository.findAnswersByQuestion(questionId);
	}
}