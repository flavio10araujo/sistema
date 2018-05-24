package com.polifono.service;

import java.util.List;

import com.polifono.domain.Answer;

public interface IAnswerService {

	public Answer save(Answer o);
	
	public Boolean delete(Integer id);
	
	public Answer findOne(int id);
	
	public List<Answer> findAll();
	
	
	public List<Answer> findByGame(int gameId);
	
	public List<Answer> findByGameAndLevel(int gameId, int levelId);
	
	public List<Answer> findByMap(int mapId);
	
	public List<Answer> findByPhase(int phaseId);
	
	public List<Answer> findByQuestion(int questionId);

}