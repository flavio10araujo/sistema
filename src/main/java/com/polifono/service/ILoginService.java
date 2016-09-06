package com.polifono.service;

import java.util.List;

import com.polifono.domain.Login;
import com.polifono.domain.Player;

public interface ILoginService {

	public Login registerLogin(Player player);
	
	public List<Login> findByPlayer(int playerId);

}