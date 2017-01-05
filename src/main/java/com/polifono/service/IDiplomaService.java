package com.polifono.service;

import java.util.List;

import com.polifono.domain.Diploma;

public interface IDiplomaService {

	public Diploma save(Diploma diploma);
	
	//public Boolean delete(Integer id);
	
	//public Diploma findOne(int diplomaId);
	
	//public List<Diploma> findAll();
	
	public List<Diploma> findByPlayer(int playerId);
	
	public Diploma findByCode(String code);
}