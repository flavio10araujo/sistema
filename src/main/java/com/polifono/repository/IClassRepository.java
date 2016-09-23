package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IClassRepository extends CrudRepository<com.polifono.domain.Class, Integer> {
	
	@Query("SELECT clazz FROM com.polifono.domain.Class clazz WHERE clazz.player.id = :playerId AND clazz.active = :status ORDER BY clazz.player.name ASC")
	public List<com.polifono.domain.Class> findClassesByTeacherAndStatus(@Param("playerId") int playerId, @Param("status") boolean status);
}