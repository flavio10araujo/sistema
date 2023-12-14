package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

public interface IClassPlayerService {

    public ClassPlayer save(ClassPlayer o);

    public Boolean delete(Integer id);

    public Optional<ClassPlayer> findById(int id);

    public List<ClassPlayer> findAll();

    public List<ClassPlayer> findByTeacher(int playerId);

    public List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId);

    public List<ClassPlayer> findByClassAndStatus(int clazzId, int status);

    public List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId);

    public List<ClassPlayer> findByPlayerAndStatus(int playerId, int status);

    public List<ClassPlayer> findByTeacherAndStudent(int teacherId, int studentId);

    public ClassPlayer create(ClassPlayer o);

    public boolean isMyStudent(Player user, Player player);

    public Boolean changeStatus(int id, int status);
}
