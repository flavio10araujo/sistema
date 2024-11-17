package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.model.entity.ClassPlayer;

public interface IClassPlayerRepository extends JpaRepository<ClassPlayer, Integer> {

    @Query("SELECT classPlayer FROM ClassPlayer classPlayer, Player player WHERE classPlayer.clazz.id = :clazzId AND classPlayer.status = :status AND classPlayer.player.id = player.id ORDER BY player.name ASC")
    List<ClassPlayer> findAllByClassIdAndStatus(@Param("clazzId") int clazzId, @Param("status") int status);

    @Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.model.entity.Class clazz, Player player WHERE classPlayer.clazz.id = clazz.id AND classPlayer.player.id = player.id AND clazz.player.id = :playerId AND classPlayer.clazz.id = :clazzId AND (classPlayer.status = 1 OR classPlayer.status = 2) ORDER BY player.name ASC")
    List<ClassPlayer> findAllByClassIdAndTeacherId(@Param("clazzId") int clazzId, @Param("playerId") int playerId);

    @Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.clazz.id = :clazzId AND classPlayer.player.id = :playerId")
    List<ClassPlayer> findAllByClassIdAndStudentId(@Param("clazzId") int clazzId, @Param("playerId") int playerId);

    @Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.model.entity.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :playerId AND (classPlayer.status = 1 OR classPlayer.status = 2)")
    List<ClassPlayer> findAllByTeacherId(@Param("playerId") int playerId);

    @Query("SELECT classPlayer FROM ClassPlayer classPlayer WHERE classPlayer.player.id = :playerId AND classPlayer.status = :status")
    List<ClassPlayer> findAllByStudentIdAndStatus(@Param("playerId") int playerId, @Param("status") int status);

    @Query("SELECT classPlayer FROM ClassPlayer classPlayer, com.polifono.model.entity.Class clazz WHERE classPlayer.clazz.id = clazz.id AND clazz.player.id = :teacherId AND classPlayer.player.id = :studentId AND classPlayer.status = 2")
    List<ClassPlayer> findAllByTeacherIdAndStudentId(@Param("teacherId") int teacherId, @Param("studentId") int studentId);
}
