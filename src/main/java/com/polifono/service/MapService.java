package com.polifono.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.repository.MapRepository;

@Service
public class MapService {

	@Autowired
	private MapRepository mapRepository;
	
	public final Map save(Map map) {
		return mapRepository.save(map);
	}
	
	public Boolean delete(Integer id) {
		Map temp = mapRepository.findOne(id);
		
		if (temp != null) {
			try {
				mapRepository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public Map find(int id) {
        return mapRepository.findOne(id);
    }

	public final List<Map> findAll() {
		return (List<Map>) mapRepository.findAll();
	}
	
	public final List<Map> findMapsByGame(int gameId) {
		return mapRepository.findMapsByGame(gameId);
	}
	
	public final List<Map> findMapsByGameAndLevel(int gameId, int levelId) {
		return mapRepository.findMapsByGameAndLevel(gameId, levelId);
	}
	
	public final Map findMapByGameAndLevel(int gameId, int levelId) {
		List<Map> maps = mapRepository.findMapsByGameAndLevel(gameId, levelId);
		
		if (maps.size() > 0) {
			return maps.get(0);
		}
		
		return null;
	}
	
	public final Map findMapsByGameLevelAndOrder(Game game, int levelId, int mapOrder) {
		Map map = mapRepository.findMapsByGameLevelAndOrder(game.getId(), levelId, mapOrder);
		return map;
	}
	
	public final Map findNextMapSameLevel(Map mapCurrent) {
		Map nextMap = mapRepository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder());
		return nextMap;
	}
}