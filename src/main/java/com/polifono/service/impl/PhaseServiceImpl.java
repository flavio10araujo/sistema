package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Phase;
import com.polifono.repository.IPhaseRepository;
import com.polifono.service.IPhaseService;

@Service
public class PhaseServiceImpl implements IPhaseService {

	@Autowired
	private IPhaseRepository repository;
	
	public final Phase save(Phase phase) {
		return repository.save(phase);
	}
	
	public Boolean delete(Integer id) {
		Phase temp = repository.findOne(id);
		
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
	
	public final Phase findOne(int phaseId) {
		return repository.findOne(phaseId);
	}
	
	public final List<Phase> findAll() {
		return (List<Phase>) repository.findAll();
	}
	
	public final List<Phase> findPhasesByGame(int gameId) {
		return repository.findPhasesByGame(gameId);
	}
	
	public final List<Phase> findPhasesByGameAndLevel(int gameId, int levelId) {
		return repository.findPhasesByGameAndLevel(gameId, levelId);
	}
	
	public final List<Phase> findPhasesByMap(int mapId) {
		return repository.findPhasesByMap(mapId);
	}
	
	public final Phase findByMapAndOrder(int mapId, int phaseOrder) {
		return repository.findPhaseByMapAndOrder(mapId, phaseOrder);
	}
	
	/**
	 * phaseOrder = it's the order of the phase that will be returned.
	 */
	public final Phase findNextPhaseInThisMap(int mapId, int phaseOrder) {
		return repository.findNextPhaseInThisMap(mapId, phaseOrder);
	}
	
	public final Phase findLastPhaseDoneByPlayerAndGame(int playerId, int gameId) {
		List<Phase> list = repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId); 
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return list.get(0);
	}
	
	public final Phase findLastPhaseOfTheLevel(int gameId, int levelId) {
		List<Phase> list = repository.findLastPhaseOfTheLevel(gameId, levelId);
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return list.get(0);
	}
	
	/**
	 * 
	 * 
	 * @param player
	 * @return
	 */
	public final List<Phase> findGamesForProfile(int playerId) {
		List<Phase> list = repository.findGamesForProfile(playerId); 
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		List<Phase> ret = new ArrayList<Phase>();
		
		int gameId = 0;
		
		for (Phase phase : list) {
			if (gameId != phase.getMap().getGame().getId()) {
				gameId = phase.getMap().getGame().getId();
				ret.add(phase);
			}
		}
		
		return ret;
	}
}