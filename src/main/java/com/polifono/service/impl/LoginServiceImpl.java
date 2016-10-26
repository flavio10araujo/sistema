package com.polifono.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Login;
import com.polifono.domain.Player;
import com.polifono.repository.ILoginRepository;
import com.polifono.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService {

	private ILoginRepository repository;
	
	@Autowired
	public LoginServiceImpl(ILoginRepository repository) {
		this.repository = repository;
	}
	
	public Login registerLogin(Player player) {
		return repository.save(prepareForRegisterLogin(player));
	}
	
	public Login prepareForRegisterLogin(Player player) {
		Login login = new Login();
		login.setPlayer(player);
		login.setDtLogin(new Date());
		return login;
	}
	
	public List<Login> findByPlayer(int playerId) {
		List<Login> list = repository.findByPlayer(playerId); 
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return list;
	}
}