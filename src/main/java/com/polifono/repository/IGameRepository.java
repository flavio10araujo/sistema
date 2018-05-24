package com.polifono.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Game;

public interface IGameRepository extends JpaRepository<Game, UUID> {

	@Query("SELECT game FROM Game game WHERE LOWER(game.namelink) LIKE LOWER(:namelink)")
	public Game findByNamelink(@Param("namelink") String namelink);
}