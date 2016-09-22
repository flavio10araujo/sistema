package com.polifono.service;

import java.util.List;

import com.polifono.domain.Question;

public interface IQuestionService {

	public Question save(Question question);
	
	public Boolean delete(Integer id);
	
	public Question findOne(int questionId);
	
	public List<Question> findAll();
	
	public List<Question> findQuestionsByGame(int gameId);
	
	public List<Question> findQuestionsByGameAndLevel(int gameId, int levelId);
	
	public List<Question> findQuestionsByMap(int mapId);
	
	public List<Question> findQuestionsByPhase(int phaseId);
	
	public List<Question> findQuestionsByContent(int contentId);
}