package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;

public interface IMapService {

	public Map save(Map map);
	
	public Boolean delete(Integer id);
	
	public Map findOne(int id);

	public List<Map> findAll();
	
	public List<Map> findMapsByGame(int gameId);
	
	public List<Map> findMapsByGameAndLevel(int gameId, int levelId);
	
	public Map findByGameAndLevel(int gameId, int levelId);
	
	public Map findByGameLevelAndOrder(int gameId, int levelId, int mapOrder);
	
	public Map findNextMapSameLevel(Map mapCurrent);

	public boolean playerCanAccessThisMap(Map map, Player user);

	public Map findCurrentMap(Game game, PlayerPhase playerPhase);

}