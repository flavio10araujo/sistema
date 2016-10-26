package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;
import com.polifono.repository.IClassPlayerRepository;
import com.polifono.service.IClassPlayerService;

@Service
public class ClassPlayerServiceImpl implements IClassPlayerService {

	private IClassPlayerRepository repository;
	
	@Autowired
	public ClassPlayerServiceImpl(IClassPlayerRepository repository) {
		this.repository = repository;
	}
	
	public final ClassPlayer save(ClassPlayer classPlayer) {
		return repository.save(classPlayer);
	}
	
	public Boolean delete(Integer id) {
		ClassPlayer temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				repository.save(prepareClassPlayerToChangeStatus(temp, 3));
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public ClassPlayer findOne(int id) {
        return repository.findOne(id);
    }

	public final List<ClassPlayer> findAll() {
		return (List<ClassPlayer>) repository.findAll();
	}
	
	public final ClassPlayer prepareClassPlayerForCreation(ClassPlayer classPlayer) {
		classPlayer.setDtInc(new Date());
		classPlayer.setActive(true);
		classPlayer.setStatus(1);
		return classPlayer;
	}
	
	public final Boolean changeStatus(int id, int status) {
		ClassPlayer temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				repository.save(prepareClassPlayerToChangeStatus(temp, status));
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public final ClassPlayer prepareClassPlayerToChangeStatus(ClassPlayer classPlayer, int status) {
		classPlayer.setStatus(status);
		return classPlayer;
	}
	
	/**
	 * Get all the students of this teacher (playerId).
	 * Get only student in the status 1 (pending) and 2 (confirmed). 
	 * 
	 * @param playerId
	 * @return
	 */
	public final List<ClassPlayer> findClassPlayersByTeacher(int playerId) {
		return repository.findClassPlayersByTeacher(playerId);
	}
	
	/**
	 * Get all the students of a specific class (clazzId) of this teacher (playerId).
	 * Get only student in the status 1 (pending) and 2 (confirmed). 
	 * 
	 * @param playerId
	 * @param clazzId
	 * @return
	 */
	public final List<ClassPlayer> findClassPlayersByTeacherAndClass(int playerId, int clazzId) {
		return repository.findClassPlayersByTeacherAndClass(playerId, clazzId);
	}
	
	public final List<ClassPlayer> findClassPlayersByClassAndStatus(int clazzId, int status) {
		return repository.findClassPlayersByClassAndStatus(clazzId, status);
	}
	
	public final List<ClassPlayer> findClassPlayersByClassAndPlayer(int clazzId, int playerId) {
		return repository.findClassPlayersByClassAndPlayer(clazzId, playerId);
	}
	
	public final List<ClassPlayer> findClassPlayersByPlayerAndStatus(int playerId, int status) {
		return repository.findClassPlayersByPlayerAndStatus(playerId, status);
	}
	
	/**
	 * Method used to see if studentId is student of teacherId in any class.
	 * The studentId must be in status 2 (confirmed).
	 * 
	 * @param teacherId
	 * @param studentId
	 * @return
	 */
	public final List<ClassPlayer> findClassPlayersByTeacherAndStudent(int teacherId, int studentId) {
		return repository.findClassPlayersByTeacherAndStudent(teacherId, studentId);
	}
	
	/**
	 * Method used to see if student is student of teacher in any class.
	 * Return true if student is student of teacher.
	 * 
	 * @param teacher
	 * @param student
	 * @return
	 */
	public boolean isMyStudent(Player teacher, Player student) {
		List<ClassPlayer> classPlayers = this.findClassPlayersByTeacherAndStudent(teacher.getId(), student.getId());
		
		if (classPlayers != null && classPlayers.size() > 0) {
			return true;
		}
		
		return false;
	}
}