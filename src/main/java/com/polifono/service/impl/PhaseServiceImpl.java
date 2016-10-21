package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.repository.IPhaseRepository;
import com.polifono.service.IPhaseService;

@Service
public class PhaseServiceImpl implements IPhaseService {

	private IPhaseRepository repository;
	
	@Autowired
	public PhaseServiceImpl(IPhaseRepository repository) {
		this.repository = repository;
	}
	
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
	
	/**
	 * Get the phases of the map.
	 * Check which phases are opened.
	 * The phases opened are: all the phase that the player has already done + next phase.
	 * 
	 * @param map
	 * @return
	 */
	public final List<Phase> findPhasesCheckedByMap(Map map, PlayerPhase lastPhaseCompleted) {
		List<Phase> phases = this.findPhasesByMap(map.getId());
		
		// If there are not phases in the map.
		if (phases == null || phases.size() == 0) {
			return null;
		}

		// If the player has never completed any phase of this game.
		if (lastPhaseCompleted == null) {
			// Open the first phase of the map.
			phases.get(0).setOpened(true);
		}
		// If the player has already completed at least one phase of this game.
		else {
			// Open all phases until the next phase.
			for (int i = 0; i < phases.size(); i++) {
				if (phases.get(i).getOrder() <= (lastPhaseCompleted.getPhase().getOrder() + 1)) {
					phases.get(i).setOpened(true);
				}
			}
		}
		
		return phases;
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
	
	public Phase findLastPhaseDoneByPlayerAndGame(int playerId, int gameId) {
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
	
	/**
	 * Verify if the player has permission to access a specific phase.
	 * Return true if the player has the permission.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean playerCanAccessThisPhase(Phase phase, Player player) {

		// The first phase is always permitted.
		if (phase.getOrder() == 1) {
			return true;
		}
		
		// Get the last phase that the player has done in a specific game.
		Phase lastPhaseDone = this.findLastPhaseDoneByPlayerAndGame(player.getId(), phase.getMap().getGame().getId());
		
		// If the player is trying to access a phase but he never had finished a phase of this game.
		if (lastPhaseDone == null) {
			return false;
		}
		
		// If the player is trying to access a phase that he had already done OR the next phase in the right sequence.
		if (lastPhaseDone.getOrder() >= (phase.getOrder() - 1)) {
			return true;
		}
		
		return false;
	}
}