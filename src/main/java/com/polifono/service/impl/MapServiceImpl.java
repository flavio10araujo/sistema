package com.polifono.service.impl;

import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.repository.IMapRepository;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;

@Service
public class MapServiceImpl implements IMapService {

	@Autowired
	private IMapRepository repository;
	
	private IPhaseService phaseService;
	
	@Autowired
	public MapServiceImpl(IPhaseService phaseService) {
		this.phaseService = phaseService;
	}
	
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
	
	/**
	 * Verify if the player has permission to access a specific map.
	 * Return true if the player has the permission.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean playerCanAccessThisMap(Map map, Player player) {

		// The first map of the first level is always permitted.
		if (map.getLevel().getOrder() == 1 && map.getOrder() == 1) {
			System.out.println("IF 1");
			return true;
		}else {System.out.println("ELSE 1");}
		
		// Get the last phase that the player has done in a specific game.
		Phase lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId());
		
		// If the player is trying to access a map different of the first map of the first level and he never had finished a phase of this game.
		if (lastPhaseDone == null) {
			System.out.println("IF 2");
			return false;
		}else{System.out.println("ELSE 2");}
		
		// If the player is trying to access a map in a previous level than the lastPhaseDone's level.
		if (map.getLevel().getOrder() < lastPhaseDone.getMap().getLevel().getOrder()) {
			System.out.println("IF 3");
			return true;
		}else {System.out.println("ELSE 3");}
		
		// If the player is trying to access a previous map (or the same map) at the same level of the lastPhaseDone.
		if (
			map.getLevel().getOrder() == lastPhaseDone.getMap().getLevel().getOrder()
			&& map.getOrder() <= lastPhaseDone.getMap().getOrder()
			) {
			System.out.println("IF 4");
			return true;
		}else {System.out.println("ELSE 4");}
		
		// If you are here, it's because the player is trying to access a next map at the same level OR a map in one of the next levels.
		
		// Get the last phase of the map of the lastPhaseDone. 
		Phase lastPhaseOfTheLevel = phaseService.findLastPhaseOfTheLevel(lastPhaseDone.getMap().getGame().getId(), lastPhaseDone.getMap().getLevel().getId());
		
		// If the lastPhaseDone is not the lastPhaseOfTheLevel. Then the player can't access the next map or the next level.
		if (lastPhaseDone.getId() != lastPhaseOfTheLevel.getId()) {
			System.out.println("IF 5");
			return false;
		}else {System.out.println("ELSE 5");}
		
		// If the player is trying to access a map in the same level.
		if (lastPhaseDone.getMap().getLevel().getOrder() == map.getLevel().getOrder()) {
			// If it is the next map.
			if ((lastPhaseDone.getMap().getOrder() + 1) == map.getOrder()) {
				System.out.println("IF 6");
				return true;
			}
			else {
				System.out.println("ELSE 6");
				return false;
			}
		}
		
		// If the player is trying to access a map in the next level.
		if ((lastPhaseDone.getMap().getLevel().getOrder() + 1) == map.getLevel().getOrder()) {
			// If it is the first map.
			if (map.getOrder() == 1) {
				System.out.println("IF 7");
				return true;
			}
			else {
				System.out.println("ELSE 7");
				return false;
			}
		}
		
		System.out.println("FIM passo 8");
		return false;
	}
	
	/**
	 * Get the current map based on the last phase completed by the player in a specific game.
	 * 
	 * @param lastPhaseCompleted
	 * @return
	 */
	public final Map findCurrentMap(Game game, PlayerPhase lastPhaseCompleted) {
		// If the player has never completed any phase of this game. 
		if (lastPhaseCompleted == null) {
			// Find the first map of the first level of this game.
			return this.findByGameAndLevel(game.getId(), 1);
		}
		// If the player has already completed at least one phase of this game.
		else {
			// Verifying is the next phase is in the same map than lastPhaseCompleted.
			Phase nextPhase = phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1);

			// If the next phase is in the same map that the last phase completed by the player in this game.
			if (nextPhase != null) {
				return lastPhaseCompleted.getPhase().getMap();
			}
			// If the next phase is in the next map or in the next level.
			else {
				// Checking if the next map is in the same level.
				Map nextMapSameLevel = this.findNextMapSameLevel(lastPhaseCompleted.getPhase().getMap());

				// If the next map is in the same level.
				if (nextMapSameLevel != null) {
					return nextMapSameLevel;
				}
				// If the next map is not in the same level. 
				else {
					// Find the first map of the next level.
					Map firstMapNextLevel = this.findByGameAndLevel(game.getId(), lastPhaseCompleted.getPhase().getMap().getLevel().getId() + 1);

					// If it has found the first map of the next level.
					if (firstMapNextLevel != null) {
						Map map = firstMapNextLevel;
						map.setLevelCompleted(true);
						return map;
					}
					// It doesn't exist a next map, because the player has already finished the last phase of the last level. 
					else {
						// Draw the last map of the last level.
						Map map = lastPhaseCompleted.getPhase().getMap();
						map.setGameCompleted(true);
						return map;
					}
				}
			}
		}
	}
}