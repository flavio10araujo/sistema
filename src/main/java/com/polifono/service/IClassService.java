package com.polifono.service;

import java.util.List;

import com.polifono.domain.Player;

public interface IClassService {

	public com.polifono.domain.Class create(com.polifono.domain.Class clazz, Player player);
	
	public com.polifono.domain.Class save(com.polifono.domain.Class clazz);
	
	public Boolean delete(Integer id);
	
	public com.polifono.domain.Class find(int id);

	public List<com.polifono.domain.Class> findAll();
	
	public List<com.polifono.domain.Class> findByTeacherAndStatus(int playerId, boolean status);

}