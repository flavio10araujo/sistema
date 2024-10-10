package com.polifono.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameRepository extends JpaRepository<PlayerGame, Integer> {

	@Query("SELECT playerGame FROM PlayerGame playerGame WHERE playerGame.player.id = :playerId AND playerGame.game.id = :gameId")
	PlayerGame findByPlayerAndGame(@Param("playerId") int playerId, @Param("gameId") int gameId);
}
