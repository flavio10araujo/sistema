package com.polifono.service;

import java.util.List;

import com.polifono.domain.Level;

public interface ILevelService {

	//public Level save(Level o);
	
	//public Boolean delete(Integer id);
		
	//public Level findOne(int id);
	
	public List<Level> findAll();
	
	
	public List<Level> findByGame(int gameId);
	
	
	public List<Level> flagLevelsToOpenedOrNot(int gameId, int levelPermitted);
}