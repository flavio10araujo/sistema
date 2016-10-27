package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Question;
import com.polifono.repository.IQuestionRepository;
import com.polifono.service.IQuestionService;

@Service
public class QuestionServiceImpl implements IQuestionService {

	private IQuestionRepository repository;
	
	@Autowired
	public QuestionServiceImpl(IQuestionRepository repository) {
		this.repository = repository;
	}
	
	public final Question save(Question question) {
		return repository.save(question);
	}
	
	public Boolean delete(Integer id) {
		Question temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				repository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final Question findOne(int questionId) {
		return repository.findOne(questionId);
	}
	
	public final List<Question> findAll() {
		return (List<Question>) repository.findAll();
	}
	
	public final List<Question> findQuestionsByGame(int gameId) {
		return repository.findQuestionsByGame(gameId);
	}
	
	public final List<Question> findQuestionsByGameAndLevel(int gameId, int levelId) {
		return repository.findQuestionsByGameAndLevel(gameId, levelId);
	}
	
	public final List<Question> findQuestionsByMap(int mapId) {
		return repository.findQuestionsByMap(mapId);
	}
	
	public final List<Question> findQuestionsByPhase(int phaseId) {
		return repository.findQuestionsByPhase(phaseId);
	}
	
	public final List<Question> findQuestionsByContent(int contentId) {
		List<Question> question = repository.findQuestionsByContent(contentId);
		return question;
	}
}