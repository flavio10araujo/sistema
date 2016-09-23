package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Player;
import com.polifono.repository.IClassRepository;
import com.polifono.service.IClassService;

@Service
public class ClassServiceImpl implements IClassService {

	@Autowired
	private IClassRepository repository;
	
	public final com.polifono.domain.Class create(com.polifono.domain.Class clazz, Player player) {
		clazz.setPlayer(player);
		clazz.setDtInc(new Date());
		clazz.setActive(true);
		return save(clazz);
	}
	
	public final com.polifono.domain.Class save(com.polifono.domain.Class clazz) {
		return repository.save(clazz);
	}
	
	public Boolean delete(Integer id) {
		com.polifono.domain.Class temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				temp.setActive(false);
				repository.save(temp);
			}
			catch (Exception e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public com.polifono.domain.Class findOne(int id) {
        return repository.findOne(id);
    }

	public final List<com.polifono.domain.Class> findAll() {
		return (List<com.polifono.domain.Class>) repository.findAll();
	}
	
	public final List<com.polifono.domain.Class> findClassesByTeacherAndStatus(int playerId, boolean status) {
		return repository.findClassesByTeacherAndStatus(playerId, status);
	}
}