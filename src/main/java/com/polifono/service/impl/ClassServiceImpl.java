package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.repository.IClassRepository;
import com.polifono.service.IClassService;

@Service
public class ClassServiceImpl implements IClassService {

	private IClassRepository repository;
	
	@Autowired
	public ClassServiceImpl(IClassRepository repository) {
		this.repository = repository;
	}
	
	public final com.polifono.domain.Class save(com.polifono.domain.Class clazz) {
		return repository.save(clazz);
	}
	
	public Boolean delete(Integer id) {
		com.polifono.domain.Class temp = repository.findOne(id);
		
		if (temp != null) {
			try {
				repository.save(prepareClassForChangingStatus(temp, false));
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
	
	public final com.polifono.domain.Class prepareClassForCreation(com.polifono.domain.Class clazz) {
		clazz.setDtInc(new Date());
		clazz.setActive(true);
		return clazz;
	}
	
	public final com.polifono.domain.Class prepareClassForChangingStatus(com.polifono.domain.Class clazz, boolean status) {
		clazz.setActive(status);
		return clazz;
	}
	
	public final List<com.polifono.domain.Class> findClassesByTeacherAndStatus(int playerId, boolean status) {
		return repository.findClassesByTeacherAndStatus(playerId, status);
	}
}