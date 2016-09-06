package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Phase;

public interface IPhaseRepository extends CrudRepository<Phase, Integer> {

	@Query("SELECT phase FROM Phase phase WHERE phase.map.id = :mapId")
	public List<Phase> findPhasesByMap(@Param("mapId") int mapId);
	
	@Query("SELECT phase FROM Phase phase, Map map WHERE phase.map.id = map.id AND map.game.id = :gameId")
	public List<Phase> findPhasesByGame(@Param("gameId") int gameId);
	
	@Query("SELECT phase FROM Phase phase, Map map WHERE phase.map.id = map.id AND map.game.id = :gameId AND map.level.id = :levelId")
	public List<Phase> findPhasesByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);
	
	@Query("SELECT phase FROM Phase phase WHERE phase.map.id = :mapId AND phase.order = :phaseOrder")
	public Phase findPhaseByMapAndOrder(@Param("mapId") int mapId, @Param("phaseOrder") int phaseOrder);
	
	@Query("SELECT phase FROM Phase phase WHERE phase.map.id = :mapId AND phase.order = :phaseOrder")
	public Phase findNextPhaseInThisMap(@Param("mapId") int mapId, @Param("phaseOrder") int phaseOrder);
	
	@Query("SELECT phase FROM PlayerPhase playerPhase, Phase phase, Map map WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND playerPhase.player.id = :playerId AND map.game.id = :gameId ORDER BY phase.order DESC")
	public List<Phase> findLastPhaseDoneByPlayerAndGame(@Param("playerId") int playerId, @Param("gameId") int gameId);
	
	@Query("SELECT phase FROM Phase phase, Map map WHERE phase.map.id = map.id AND map.game.id = :gameId AND map.level.id = :levelId ORDER BY phase.order DESC")
	public List<Phase> findLastPhaseOfTheLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);
	
	@Query("SELECT phase FROM PlayerPhase playerPhase, Phase phase, Map map, Game game WHERE playerPhase.phase.id = phase.id AND phase.map.id = map.id AND map.game.id = game.id AND playerPhase.player.id = :playerId ORDER BY game.id, phase.id DESC")
	public List<Phase> findGamesForProfile(@Param("playerId") int playerId);
}