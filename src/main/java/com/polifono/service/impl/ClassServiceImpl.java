package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Player;
import com.polifono.repository.IClassRepository;

@Service
public class ClassServiceImpl {

	@Autowired
	private IClassRepository classRepository;
	
	public final com.polifono.domain.Class create(com.polifono.domain.Class clazz, Player player) {
		clazz.setPlayer(player);
		clazz.setDtInc(new Date());
		clazz.setActive(true);
		return save(clazz);
	}
	
	public final com.polifono.domain.Class save(com.polifono.domain.Class clazz) {
		return classRepository.save(clazz);
	}
	
	public Boolean delete(Integer id) {
		com.polifono.domain.Class temp = classRepository.findOne(id);
		
		if (temp != null) {
			try {
				temp.setActive(false);
				classRepository.save(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public com.polifono.domain.Class find(int id) {
        return classRepository.findOne(id);
    }

	public final List<com.polifono.domain.Class> findAll() {
		return (List<com.polifono.domain.Class>) classRepository.findAll();
	}
	
	public final List<com.polifono.domain.Class> findByTeacherAndStatus(int playerId, boolean status) {
		return classRepository.findByTeacherAndStatus(playerId, status);
	}
}