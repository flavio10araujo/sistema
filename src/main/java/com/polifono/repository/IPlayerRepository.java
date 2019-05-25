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
	
	@Query("SELECT player FROM Player player WHERE DATE(player.dtInc) >= DATE(:dateBegin) AND DATE(player.dtInc) <= DATE(:dateEnd)")
	public List<Player> findByDateIncRange(@Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);
	
	/**
	 * GROUP 04
	 * - Players that have never bought credits.
	 * - Players that have been made their register at least one month ago.
	 * - Players that didn't receive one message of type 02 in the last 2 months.
	 */
	@Query(value = "select * from t001_player t1 where "
			+ "date(t1.c001_dt_inc) < '2019-04-23' and "
			+ "t1.c001_active = 1 and "
			+ "t1.c001_role = 'user' and "
			+ "t1.c001_id_creator is null and "
			+ "t1.c001_email is not null and "
			+ "t1.c001_email != '' and "
			+ "(select count(1) from t020_communication t20, t021_player_communication t21 where t20.c020_id = t21.c020_id and t20.c019_id = 4 and date(t20.c020_dt_inc) >= '2019-03-23' and t21.c001_id = t1.c001_id) = 0 and "
			+ "(select count(1) from t012_transaction t12 where t12.c012_status = 3 and t12.c001_id = t1.c001_id) = 0 "
			+ "limit 10", nativeQuery = true)
	public List<Player> findCommunicationGroup04();

}