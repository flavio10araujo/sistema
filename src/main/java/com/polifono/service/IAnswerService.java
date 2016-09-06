package com.polifono.service;

import java.util.List;

import com.polifono.domain.Answer;

public interface IAnswerService {

	public Answer save(Answer answer);
	
	public Boolean delete(Integer id);
	
	public Answer find(int answerId);
	
	public List<Answer> findAll();
	
	public List<Answer> findAnswersByGame(int gameId);
	
	public List<Answer> findAnswersByGameAndLevel(int gameId, int levelId);
	
	public List<Answer> findAnswersByMap(int mapId);
	
	public List<Answer> findAnswersByPhase(int phaseId);
	
	public List<Answer> findAnswersByQuestion(int questionId);

}