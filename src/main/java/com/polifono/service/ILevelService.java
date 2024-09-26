package com.polifono.service;

import java.util.List;

import com.polifono.domain.Level;

public interface ILevelService {

	List<Level> findAll();

	List<Level> findByActive(boolean active);

	List<Level> findByGame(int gameId);

	List<Level> flagLevelsToOpenedOrNot(int gameId, int levelPermitted);
}
