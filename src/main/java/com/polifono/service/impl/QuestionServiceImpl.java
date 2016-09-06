package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Question;
import com.polifono.repository.IQuestionRepository;

@Service
public class QuestionServiceImpl {

	@Autowired
	private IQuestionRepository questionRepository;
	
	public final Question save(Question question) {
		return questionRepository.save(question);
	}
	
	public Boolean delete(Integer id) {
		Question temp = questionRepository.findOne(id);
		
		if (temp != null) {
			try {
				questionRepository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final Question find(int questionId) {
		return questionRepository.findOne(questionId);
	}
	
	public final List<Question> findAll() {
		return (List<Question>) questionRepository.findAll();
	}
	
	public final List<Question> findQuestionsByGame(int gameId) {
		return questionRepository.findQuestionsByGame(gameId);
	}
	
	public final List<Question> findQuestionsByGameAndLevel(int gameId, int levelId) {
		return questionRepository.findQuestionsByGameAndLevel(gameId, levelId);
	}
	
	public final List<Question> findQuestionsByMap(int mapId) {
		return questionRepository.findQuestionsByMap(mapId);
	}
	
	public final List<Question> findQuestionsByPhase(int phaseId) {
		return questionRepository.findQuestionsByPhase(phaseId);
	}
	
	public final List<Question> findQuestionsByContent(int contentId) {
		List<Question> question = questionRepository.findQuestionsByContent(contentId);
		return question;
	}
}