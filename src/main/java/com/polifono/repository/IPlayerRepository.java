package com.polifono.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Player;

public interface IPlayerRepository extends CrudRepository<Player, Integer> {

	@Query("SELECT player FROM Player player WHERE player.email = :email AND player.active = :status")
	public Optional<Player> findUserByEmailAndStatusForLogin(@Param("email") String email, @Param("status") boolean status);
	
	@Query("SELECT player FROM Player player WHERE player.email = :email AND player.active = :status")
	public Player findUserByEmailAndStatus(@Param("email") String email, @Param("status") boolean status);
	
	@Query("SELECT player FROM Player player WHERE player.email = :email")
	public Player findPlayerByEmail(@Param("email") String email);
}