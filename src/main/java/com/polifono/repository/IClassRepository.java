package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.model.entity.Class;

public interface IClassRepository extends JpaRepository<Class, Integer> {

    @Query("SELECT clazz FROM com.polifono.model.entity.Class clazz WHERE clazz.player.id = :playerId AND clazz.active = :status ORDER BY clazz.year DESC, clazz.school, clazz.grade")
    List<Class> findByTeacherAndStatus(@Param("playerId") int playerId, @Param("status") boolean status);
}
