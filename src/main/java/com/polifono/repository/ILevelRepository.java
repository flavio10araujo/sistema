package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Level;

public interface ILevelRepository extends JpaRepository<Level, Integer> {

	@Query("SELECT level FROM Level level WHERE level.active = :active")
	List<Level> findByActive(@Param("active") boolean active);

	@Query("SELECT DISTINCT level FROM Level level, Phase phase, Map map WHERE phase.map.id = map.id AND map.level.id = level.id AND map.game.id = :gameId")
	List<Level> findByGame(@Param("gameId") int gameId);
}
