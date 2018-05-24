package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Answer;
import com.polifono.repository.IAnswerRepository;
import com.polifono.service.IAnswerService;

@Service
public class AnswerServiceImpl implements IAnswerService {

	private IAnswerRepository repository;
	
	@Autowired
	public AnswerServiceImpl(IAnswerRepository repository) {
		this.repository = repository;
	}
	
	public final Answer save(Answer answer) {
		return repository.save(answer);
	}
	
	public Boolean delete(Integer id) {
		Answer temp = repository.findOne(id);
		
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
	
	public final Answer findOne(int answerId) {
		return repository.findOne(answerId);
	}
	
	public final List<Answer> findAll() {
		return (List<Answer>) repository.findAll();
	}
	
	public final List<Answer> findByGame(int gameId) {
		return repository.findByGame(gameId);
	}
	
	public final List<Answer> findByGameAndLevel(int gameId, int levelId) {
		return repository.findByGameAndLevel(gameId, levelId);
	}
	
	public final List<Answer> findByMap(int mapId) {
		return repository.findByMap(mapId);
	}
	
	public final List<Answer> findByPhase(int phaseId) {
		return repository.findByPhase(phaseId);
	}
	
	public final List<Answer> findByQuestion(int questionId) {
		return repository.findByQuestion(questionId);
	}
}