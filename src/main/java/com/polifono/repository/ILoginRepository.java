package com.polifono.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Login;

public interface ILoginRepository extends CrudRepository<Login, Integer> {

	//@Query("SELECT login FROM Login login WHERE login.player.id = :playerId ORDER BY login.dtLogin DESC")
	
	@Query("SELECT DISTINCT(DATE(C013_DT_LOGIN)) AS C013_DT_LOGIN FROM Login login WHERE login.player.id = :playerId ORDER BY C013_DT_LOGIN DESC")
	public List<Date> findByPlayer(@Param("playerId") int playerId, Pageable pageable);
}