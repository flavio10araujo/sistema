package com.polifono.service;

import java.util.List;

import com.polifono.domain.ClassPlayer;

public interface IClassPlayerService {

	public ClassPlayer create(ClassPlayer classPlayer);
	
	public ClassPlayer save(ClassPlayer classPlayer);
	
	public Boolean delete(Integer id);
	
	public ClassPlayer find(int id);

	public List<ClassPlayer> findAll();
	
	public Boolean changeStatus(int id, int status);
	
	public List<ClassPlayer> findByTeacher(int playerId);
	
	public List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId);
	
	public List<ClassPlayer> findByClassAndStatus(int clazzId, int status);
	
	public List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId);
	
	public List<ClassPlayer> findByPlayerAndStatus(int playerId, int status);
	
	public List<ClassPlayer> findByTeacherAndStudent(int teacherId, int studentId);

}