package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Level;
import com.polifono.repository.ILevelRepository;
import com.polifono.service.ILevelService;

@Service
public class LevelServiceImpl implements ILevelService {

	private ILevelRepository repository;
	
	@Autowired
	public LevelServiceImpl(ILevelRepository repository) {
		this.repository = repository;
	}
	
	public final List<Level> findAll() {
		return (List<Level>) repository.findAll();
	}
	
	public List<Level> findByGame(int gameId) {
		return (List<Level>) repository.findByGame(gameId);
	}
	
	/**
	 * Verify which levels are opened.
	 * Catch all the levels of a game and, based on the last level permitted to the player (levelPermitted), check or not the flag opened in each level.
	 * 
	 * @param gameId
	 * @param levelPermitted
	 * @return
	 */
	public List<Level> flagLevelsToOpenedOrNot(int gameId, int levelPermitted) {
		List<Level> levelsAux = this.findByGame(gameId);
		List<Level> levels = new ArrayList<Level>();
		
		for (Level level : levelsAux) {
			if (level.getOrder() <= levelPermitted) {
				level.setOpened(true);
			}
			levels.add(level);
		}
		
		return levels;
	}
}