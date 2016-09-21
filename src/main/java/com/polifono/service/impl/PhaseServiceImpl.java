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
	private IPhaseRepository phaseRepository;
	
	public final Phase save(Phase phase) {
		return phaseRepository.save(phase);
	}
	
	public Boolean delete(Integer id) {
		Phase temp = phaseRepository.findOne(id);
		
		if (temp != null) {
			try {
				phaseRepository.delete(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final Phase findOne(int phaseId) {
		return phaseRepository.findOne(phaseId);
	}
	
	public final List<Phase> findAll() {
		return (List<Phase>) phaseRepository.findAll();
	}
	
	public final List<Phase> findPhasesByGame(int gameId) {
		return phaseRepository.findPhasesByGame(gameId);
	}
	
	public final List<Phase> findPhasesByGameAndLevel(int gameId, int levelId) {
		return phaseRepository.findPhasesByGameAndLevel(gameId, levelId);
	}
	
	public final List<Phase> findPhasesByMap(int mapId) {
		return phaseRepository.findPhasesByMap(mapId);
	}
	
	public final Phase findByMapAndOrder(int mapId, int phaseOrder) {
		return phaseRepository.findPhaseByMapAndOrder(mapId, phaseOrder);
	}
	
	/**
	 * phaseOrder = it's the order of the phase that will be returned.
	 */
	public final Phase findNextPhaseInThisMap(int mapId, int phaseOrder) {
		return phaseRepository.findNextPhaseInThisMap(mapId, phaseOrder);
	}
	
	public final Phase findLastPhaseDoneByPlayerAndGame(int playerId, int gameId) {
		List<Phase> list = phaseRepository.findLastPhaseDoneByPlayerAndGame(playerId, gameId); 
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return list.get(0);
	}
	
	public final Phase findLastPhaseOfTheLevel(int gameId, int levelId) {
		List<Phase> list = phaseRepository.findLastPhaseOfTheLevel(gameId, levelId);
		
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
		List<Phase> list = phaseRepository.findGamesForProfile(playerId); 
		
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