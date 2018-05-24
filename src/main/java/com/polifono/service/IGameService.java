package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;

public interface IGameService {

	//public Game save(Game o);
	
	//public Boolean delete(Integer id);
	
	//public Game findOne(int id);
		
	public List<Game> findAll();
	
	
	public Game findByNamelink(String namelink);

	
	public int calculateScore(int numAttempts, int grade);
	
	public int calculateGrade(List<Integer> questionsId, java.util.Map<String, String> playerAnswers);
	
	public Phase getPhaseOfTheTest(List<Integer> questionsId);

}