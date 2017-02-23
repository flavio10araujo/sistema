package com.polifono.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameRepository extends CrudRepository<PlayerGame, Integer> {

	@Query("SELECT playerGame FROM PlayerGame playerGame WHERE playerGame.player.id = :playerId AND playerGame.game.id = :gameId")
	public PlayerGame findByPlayerAndGame(@Param("playerId") int playerId, @Param("gameId") int gameId);
}