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
	
	public final List<Answer> findAnswersByGame(int gameId) {
		return repository.findAnswersByGame(gameId);
	}
	
	public final List<Answer> findAnswersByGameAndLevel(int gameId, int levelId) {
		return repository.findAnswersByGameAndLevel(gameId, levelId);
	}
	
	public final List<Answer> findAnswersByMap(int mapId) {
		return repository.findAnswersByMap(mapId);
	}
	
	public final List<Answer> findAnswersByPhase(int phaseId) {
		return repository.findAnswersByPhase(phaseId);
	}
	
	public final List<Answer> findAnswersByQuestion(int questionId) {
		return repository.findAnswersByQuestion(questionId);
	}
}