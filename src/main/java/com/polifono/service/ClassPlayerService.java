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
				classPlayerRepository.delete(temp);
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
	
	public final List<ClassPlayer> findByTeacher(int playerId) {
		return classPlayerRepository.findByTeacher(playerId);
	}
	
	public final List<ClassPlayer> findByTeacherAndClass(int playerId, int clazzId) {
		return classPlayerRepository.findByTeacherAndClass(playerId, clazzId);
	}
	
	public final List<ClassPlayer> findByClassAndPlayer(int classId, int playerId) {
		return classPlayerRepository.findByClassAndPlayer(classId, playerId);
	}
	
	public final List<ClassPlayer> findByPlayerAndStatus(int playerId, int status) {
		return classPlayerRepository.findByPlayerAndStatus(playerId, status);
	}
}