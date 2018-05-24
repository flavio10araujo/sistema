package com.polifono.service;

import java.util.List;

import com.polifono.domain.Diploma;

public interface IDiplomaService {

	public Diploma save(Diploma o);
	
	//public Boolean delete(Integer id);
	
	//public Diploma findOne(int id);
	
	//public List<Diploma> findAll();
	
	
	public Diploma findByCode(String code);
	
	public List<Diploma> findByPlayer(int playerId);
	
}