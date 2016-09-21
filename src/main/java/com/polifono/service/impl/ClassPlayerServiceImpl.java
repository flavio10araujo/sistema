package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.ClassPlayer;
import com.polifono.repository.IClassPlayerRepository;
import com.polifono.service.IClassPlayerService;

@Service
public class ClassPlayerServiceImpl implements IClassPlayerService {

	@Autowired
	private IClassPlayerRepository repository;
	
	public final ClassPlayer create(ClassPlayer classPlayer) {
		classPlayer.setDtInc(new Date());
		classPlayer.setActive(true);
		classPlayer.setStatus(1);
		return save(classPlayer);
	}
	
	public final ClassPlayer save(ClassPlayer classPlayer) {
		return repository.save(classPlayer);
	}
	
	public Boolean delete(Integer id) {
		ClassPlayer temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				temp.setStatus(3);
				repository.save(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public ClassPlayer find(int id) {
        return repository.findOne(id);
    }

	public final List<ClassPlayer> findAll() {
		return (List<ClassPlayer>) repository.findAll();
	}
	
	public Boolean changeStatus(int id, int status) {
		ClassPlayer temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				temp.setStatus(status);
				repository.save(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get all the students of this teacher (playerId).
	 * Get only student in the status 1 (pending) and 2 (confirmed). 
	 * 
	 * @param playerId
	 * @return
	 */
	public final List<ClassPlayer> findByTeacher(int playerId) {
		return repository.findByTeacher(playerId);
	}
	
	/**
	 * Get all the students of a specific class (clazzId) of this teacher (playerId).
	 * Get only student in the status 1 (pending) and 2 (confirmed). 
	 * 
	 * @param playerId
	 * @param clazzId
	 * @return
	 */
	public final List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId) {
		return repository.findByTeacherAndClass(playerId, clazzId);
	}
	
	public final List<ClassPlayer> findByClassAndStatus(int clazzId, int status) {
		return repository.findByClassAndStatus(clazzId, status);
	}
	
	public final List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId) {
		return repository.findByClassAndPlayer(clazzId, playerId);
	}
	
	public final List<ClassPlayer> findByPlayerAndStatus(int playerId, int status) {
		return repository.findByPlayerAndStatus(playerId, status);
	}
	
	/**
	 * Method used to see if studentId is student of teacherId in any class.
	 * The studentId must be in status 2 (confirmed).
	 * 
	 * @param teacherId
	 * @param studentId
	 * @return
	 */
	public final List<ClassPlayer> findByTeacherAndStudent(int teacherId, int studentId) {
		return repository.findByTeacherAndStudent(teacherId, studentId);
	}
}