package com.polifono.service;

import java.util.List;

import com.polifono.domain.Map;

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

}