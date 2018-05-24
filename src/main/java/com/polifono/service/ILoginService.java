package com.polifono.service;

import java.util.List;

import com.polifono.domain.Login;
import com.polifono.domain.Player;

public interface ILoginService {

	//public Login save(Login o);
	
	//public Boolean delete(Integer id);
			
	//public Login findOne(int id);
	
	//public List<Login> findAll();
	
	
	public List<Login> findByPlayer(int playerId);
	
	
	public Login registerLogin(Player player);

}