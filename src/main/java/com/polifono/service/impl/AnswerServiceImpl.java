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
	
	@Override
	public final Answer save(Answer answer) {
		return repository.save(answer);
	}
	
	@Override
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
	
	@Override
	public final Answer findOne(int answerId) {
		return repository.findOne(answerId);
	}
	
	@Override
	public final List<Answer> findAll() {
		return (List<Answer>) repository.findAll();
	}
	
	@Override
	public final List<Answer> findByGame(int gameId) {
		return repository.findByGame(gameId);
	}
	
	@Override
	public final List<Answer> findByGameAndLevel(int gameId, int levelId) {
		return repository.findByGameAndLevel(gameId, levelId);
	}
	
	@Override
	public final List<Answer> findByMap(int mapId) {
		return repository.findByMap(mapId);
	}
	
	@Override
	public final List<Answer> findByPhase(int phaseId) {
		return repository.findByPhase(phaseId);
	}
	
	@Override
	public final List<Answer> findByQuestion(int questionId) {
		return repository.findByQuestion(questionId);
	}
}