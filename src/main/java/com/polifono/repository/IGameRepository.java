package com.polifono.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Game;

public interface IGameRepository extends JpaRepository<Game, UUID> {

	@Query("SELECT game FROM Game game WHERE game.active = :active")
	public List<Game> findByActive(@Param("active") boolean active);
	
	@Query("SELECT game FROM Game game WHERE LOWER(game.namelink) LIKE LOWER(:namelink)")
	public Game findByNamelink(@Param("namelink") String namelink);
}