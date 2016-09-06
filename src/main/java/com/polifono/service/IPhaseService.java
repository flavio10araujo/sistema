package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;

public interface IPhaseService {

	public Phase save(Phase phase);
	
	public Boolean delete(Integer id);
	
	public Phase find(int phaseId);
	
	public List<Phase> findAll();
	
	public List<Phase> findPhasesByGame(int gameId);
	
	public List<Phase> findPhasesByGameAndLevel(int gameId, int levelId);
	
	public List<Phase> findPhasesByMap(int mapId);
	
	public Phase findPhaseByMapAndOrder(Map map, int phaseOrder);
	
	public Phase findNextPhaseInThisMap(Map map, int phaseOrder);
	
	public Phase findLastPhaseDoneByPlayerAndGame(Player player, Game game);
	
	public Phase findLastPhaseOfTheLevel(int gameId, int levelId);
	
	public List<Phase> findGamesForProfile(Player player);
}