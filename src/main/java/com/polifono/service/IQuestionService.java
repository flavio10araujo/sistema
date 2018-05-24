package com.polifono.service;

import java.util.List;

import com.polifono.domain.Question;

public interface IQuestionService {

	public Question save(Question question);
	
	public Boolean delete(Integer id);
	
	public Question findOne(int questionId);
	
	public List<Question> findAll();
	
	public List<Question> findByGame(int gameId);
	
	public List<Question> findByGameAndLevel(int gameId, int levelId);
	
	public List<Question> findByMap(int mapId);
	
	public List<Question> findByPhase(int phaseId);
	
	public List<Question> findByContent(int contentId);
}