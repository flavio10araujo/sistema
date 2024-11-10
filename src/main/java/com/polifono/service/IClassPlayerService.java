package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.model.entity.ClassPlayer;
import com.polifono.model.entity.Player;

public interface IClassPlayerService {

    ClassPlayer save(ClassPlayer o);

    boolean delete(Integer id);

    Optional<ClassPlayer> findById(int id);

    List<ClassPlayer> findAll();

    List<ClassPlayer> findAllByClassIdAndStatus(int clazzId, int status);

    List<ClassPlayer> findAllByClassIdAndTeacherId(int clazzId, int teacherId);

    List<ClassPlayer> findAllByClassIdAndStudentId(int clazzId, int studentId);

    List<ClassPlayer> findAllByTeacherId(int teacherId);

    List<ClassPlayer> findAllByTeacherIdAndStudentId(int teacherId, int studentId);

    List<ClassPlayer> findAllByStudentIdAndStatus(int studentId, int status);

    ClassPlayer create(ClassPlayer o);

    boolean isMyStudent(Player user, Player player);

    boolean changeStatus(int id, int status);
}
