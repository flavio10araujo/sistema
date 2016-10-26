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
	
	public List<ClassPlayer> findClassPlayersByTeacher(int playerId);
	
	public List<ClassPlayer> findClassPlayersByTeacherAndClass(int playerId, int clazzId);
	
	public List<ClassPlayer> findClassPlayersByClassAndStatus(int clazzId, int status);
	
	public List<ClassPlayer> findClassPlayersByClassAndPlayer(int clazzId, int playerId);
	
	public List<ClassPlayer> findClassPlayersByPlayerAndStatus(int playerId, int status);
	
	public List<ClassPlayer> findClassPlayersByTeacherAndStudent(int teacherId, int studentId);

	public boolean isMyStudent(Player user, Player player);

}