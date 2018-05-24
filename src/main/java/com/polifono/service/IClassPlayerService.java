package com.polifono.service;

import java.util.List;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

public interface IClassPlayerService {
	
	public ClassPlayer save(ClassPlayer classPlayer);
	
	public Boolean delete(Integer id);
	
	public ClassPlayer findOne(int id);

	public List<ClassPlayer> findAll();
	
	public ClassPlayer prepareClassPlayerForCreation(ClassPlayer classPlayer);
	
	public Boolean changeStatus(int id, int status);
	
	public ClassPlayer prepareClassPlayerToChangeStatus(ClassPlayer classPlayer, int status);
	
	public List<ClassPlayer> findByTeacher(int playerId);
	
	public List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId);
	
	public List<ClassPlayer> findByClassAndStatus(int clazzId, int status);
	
	public List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId);
	
	public List<ClassPlayer> findByPlayerAndStatus(int playerId, int status);
	
	public List<ClassPlayer> findByTeacherAndStudent(int teacherId, int studentId);

	public boolean isMyStudent(Player user, Player player);

}