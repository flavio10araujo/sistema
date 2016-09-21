package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Map;
import com.polifono.repository.IMapRepository;
import com.polifono.service.IMapService;

@Service
public class MapServiceImpl implements IMapService {

	@Autowired
	private IMapRepository repository;
	
	public final Map save(Map map) {
		return repository.save(map);
	}
	
	public Boolean delete(Integer id) {
		Map temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				repository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public Map findOne(int id) {
        return repository.findOne(id);
    }

	public final List<Map> findAll() {
		return (List<Map>) repository.findAll();
	}
	
	public final List<Map> findMapsByGame(int gameId) {
		return repository.findMapsByGame(gameId);
	}
	
	public final List<Map> findMapsByGameAndLevel(int gameId, int levelId) {
		return repository.findMapsByGameAndLevel(gameId, levelId);
	}
	
	public final Map findByGameAndLevel(int gameId, int levelId) {
		List<Map> maps = repository.findMapsByGameAndLevel(gameId, levelId);
		
		if (maps.size() > 0) {
			return maps.get(0);
		}
		
		return null;
	}
	
	public final Map findByGameLevelAndOrder(int gameId, int levelId, int mapOrder) {
		List<Map> maps = repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder);
		
		if (maps.size() > 0) {
			return maps.get(0);
		}
		
		return null;
	}
	
	public final Map findNextMapSameLevel(Map mapCurrent) {
		Map nextMap = repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder());
		return nextMap;
	}
}