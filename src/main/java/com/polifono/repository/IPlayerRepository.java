package com.polifono.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Player;

public interface IPlayerRepository extends JpaRepository<Player, Integer> {

	@Query("SELECT player FROM Player player WHERE player.email = :email AND player.active = :status")
	public Optional<Player> findByEmailAndStatusForLogin(@Param("email") String email, @Param("status") boolean status);
	
	@Query("SELECT player FROM Player player WHERE player.login = :login AND player.active = :status")
	public Optional<Player> findByLoginAndStatusForLogin(@Param("login") String login, @Param("status") boolean status);
	
	public Player findByEmailAndActive(String email, boolean active);
	
	public Player findByEmail(String email);
	
	public Player findByLogin(String login);
	
	public Player findByIdFacebook(Long idFacebook);
	
	@Query("SELECT player FROM Player player WHERE player.dtInc >= :dateBegin AND DATE(player.dtInc) <= :dateEnd")
	public List<Player> findByDateIncRange(@Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);

}