package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.ClassPlayer;

public interface ClassPlayerRepository extends CrudRepository<ClassPlayer, Integer> {

	@Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.domain.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :playerId")
	public List<ClassPlayer> findByTeacher(@Param("playerId") int playerId);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.domain.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :playerId AND classPlayer.clazz.id = :clazzId")
	public List<ClassPlayer> findByTeacherAndClass(@Param("playerId") int playerId, @Param("clazzId") int clazzId);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.clazz.id = :clazzId AND classPlayer.player.id = :playerId")
	public List<ClassPlayer> findByClassAndPlayer(@Param("clazzId") int clazzId, @Param("playerId") int playerId);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.player.id = :playerId AND classPlayer.status = :status")
	public List<ClassPlayer> findByPlayerAndStatus(@Param("playerId") int playerId, @Param("status") int status);
}