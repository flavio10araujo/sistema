package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Diploma;
import com.polifono.repository.IDiplomaRepository;
import com.polifono.service.IDiplomaService;

@Service
public class DiplomaServiceImpl implements IDiplomaService {

	private IDiplomaRepository repository;
	
	@Autowired
	public DiplomaServiceImpl(IDiplomaRepository repository) {
		this.repository = repository;
	}
	
	public final Diploma save(Diploma diploma) {
		return repository.save(diploma);
	}

	@Override
	public List<Diploma> findByPlayer(int playerId) {
		List<Diploma> list = repository.findByPlayer(playerId); 
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return list;
	}
	
	@Override
	public Diploma findByCode(String code) {
		return repository.findByCode(code);
	}
}