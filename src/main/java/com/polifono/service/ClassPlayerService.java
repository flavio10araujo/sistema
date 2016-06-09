package com.polifono.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.ClassPlayer;
import com.polifono.repository.ClassPlayerRepository;

@Service
public class ClassPlayerService {

	@Autowired
	private ClassPlayerRepository classPlayerRepository;
	
	public final ClassPlayer create(ClassPlayer classPlayer) {
		classPlayer.setDtInc(new Date());
		classPlayer.setActive(true);
		classPlayer.setStatus(1);
		return save(classPlayer);
	}
	
	public final ClassPlayer save(ClassPlayer classPlayer) {
		return classPlayerRepository.save(classPlayer);
	}
	
	public Boolean delete(Integer id) {
		ClassPlayer temp = classPlayerRepository.findOne(id);
		
		if (temp != null) {
			try {
				temp.setStatus(3);
				classPlayerRepository.save(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public ClassPlayer find(int id) {
        return classPlayerRepository.findOne(id);
    }

	public final List<ClassPlayer> findAll() {
		return (List<ClassPlayer>) classPlayerRepository.findAll();
	}
	
	public Boolean changeStatus(int id, int status) {
		ClassPlayer temp = classPlayerRepository.findOne(id);
		
		if (temp != null) {
			try {
				temp.setStatus(status);
				classPlayerRepository.save(temp);
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
		return classPlayerRepository.findByTeacher(playerId);
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
		return classPlayerRepository.findByTeacherAndClass(playerId, clazzId);
	}
	
	public final List<ClassPlayer> findByClassAndStatus(int clazzId, int status) {
		return classPlayerRepository.findByClassAndStatus(clazzId, status);
	}
	
	public final List<ClassPlayer> findByClassAndPlayer(int clazzId, int playerId) {
		return classPlayerRepository.findByClassAndPlayer(clazzId, playerId);
	}
	
	public final List<ClassPlayer> findByPlayerAndStatus(int playerId, int status) {
		return classPlayerRepository.findByPlayerAndStatus(playerId, status);
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
		return classPlayerRepository.findByTeacherAndStudent(teacherId, studentId);
	}
}