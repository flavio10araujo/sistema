package com.polifono.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

	@Query("SELECT player FROM Player player WHERE player.email = :email")
	public Optional<Player> findUserByEmail(@Param("email") String email);
	
	@Query("SELECT player FROM Player player WHERE player.email = :email")
	public Player findPlayerByEmail(@Param("email") String email);
}