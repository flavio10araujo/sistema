package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polifono.domain.Level;
import com.polifono.repository.ILevelRepository;
import com.polifono.service.ILevelService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LevelServiceImpl implements ILevelService {

	private final ILevelRepository repository;

	public final List<Level> findAll() {
		return repository.findAll();
	}

	public List<Level> findByGame(int gameId) {
		return repository.findByGame(gameId);
	}

	@Override
	public List<Level> findByActive(boolean active) {
		return repository.findByActive(active);
	}

	/**
	 * Verify which levels are opened.
	 * Catch all the levels of a game and, based on the last level permitted to the player (levelPermitted), check or not the flag opened in each level.
	 */
	public List<Level> flagLevelsToOpenedOrNot(int gameId, int levelPermitted) {
		List<Level> levelsAux = this.findByGame(gameId);
		List<Level> levels = new ArrayList<>();

		for (Level level : levelsAux) {
			if (level.getOrder() <= levelPermitted) {
				level.setOpened(true);
			}
			levels.add(level);
		}

		return levels;
	}
}
