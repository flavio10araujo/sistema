package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Login;

public interface ILoginRepository extends CrudRepository<Login, Integer> {

	@Query("SELECT login FROM Login login WHERE login.player.id = :playerId ORDER BY login.dtLogin DESC")
	public List<Login> findByPlayer(@Param("playerId") int playerId);
}