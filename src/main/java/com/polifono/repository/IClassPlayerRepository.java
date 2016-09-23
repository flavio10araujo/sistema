package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.ClassPlayer;

public interface IClassPlayerRepository extends CrudRepository<ClassPlayer, Integer> {

	@Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.domain.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :playerId AND (classPlayer.status = 1 OR classPlayer.status = 2)")
	public List<ClassPlayer> findClassPlayersByTeacher(@Param("playerId") int playerId);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.clazz.id = :clazzId AND classPlayer.status = :status")
	public List<ClassPlayer> findClassPlayersByClassAndStatus(@Param("clazzId") int clazzId, @Param("status") int status);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.domain.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :playerId AND classPlayer.clazz.id = :clazzId AND (classPlayer.status = 1 OR classPlayer.status = 2)")
	public List<ClassPlayer> findClassPlayersByTeacherAndClass(@Param("playerId") int playerId, @Param("clazzId") int clazzId);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.clazz.id = :clazzId AND classPlayer.player.id = :playerId")
	public List<ClassPlayer> findClassPlayersByClassAndPlayer(@Param("clazzId") int clazzId, @Param("playerId") int playerId);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.player.id = :playerId AND classPlayer.status = :status")
	public List<ClassPlayer> findClassPlayersByPlayerAndStatus(@Param("playerId") int playerId, @Param("status") int status);
	
	@Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.domain.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :teacherId AND classPlayer.player.id = :studentId AND classPlayer.status = 2")
	public List<ClassPlayer> findClassPlayersByTeacherAndStudent(@Param("teacherId") int teacherId, @Param("studentId") int studentId);
}