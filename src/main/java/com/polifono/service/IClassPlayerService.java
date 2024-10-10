package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

public interface IClassPlayerService {

    ClassPlayer save(ClassPlayer o);

    boolean delete(Integer id);

    Optional<ClassPlayer> findById(int id);

    List<ClassPlayer> findAll();

    List<ClassPlayer> findByTeacher(int playerId);

    List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId);

    List<ClassPlayer> findByClassAndStatus(int clazzId, int status);

    List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId);

    List<ClassPlayer> findByPlayerAndStatus(int playerId, int status);

    List<ClassPlayer> findByTeacherAndStudent(int teacherId, int studentId);

    ClassPlayer create(ClassPlayer o);

    boolean isMyStudent(Player user, Player player);

    boolean changeStatus(int id, int status);
}
