package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Map;

public interface IMapRepository extends CrudRepository<Map, Integer> {

	@Query("SELECT map FROM Map map WHERE map.game.id = :gameId ORDER BY map.order ASC")
	public List<Map> findMapsByGame(@Param("gameId") int gameId);
	
	@Query("SELECT map FROM Map map WHERE map.game.id = :gameId AND map.level.id = :levelId ORDER BY map.order ASC")
	public List<Map> findMapsByGameAndLevel(@Param("gameId") int gameId, @Param("levelId") int levelId);
	
	@Query("SELECT map FROM Map map WHERE map.game.id = :gameId AND map.level.id = :levelId AND map.order = :mapOrder")
	public List<Map> findMapsByGameLevelAndOrder(@Param("gameId") int gameId, @Param("levelId") int levelId, @Param("mapOrder") int mapOrder);
	
	@Query("SELECT map FROM Map map WHERE map.game.id = :gameId AND map.level.id = :levelId AND map.order > :mapOrder")
	public Map findNextMapSameLevel(@Param("gameId") int gameId, @Param("levelId") int levelId, @Param("mapOrder") int mapOrder);
}