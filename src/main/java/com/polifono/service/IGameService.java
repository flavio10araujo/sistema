package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;

public interface IGameService {

	public List<Game> findAll();
	
	public Game findByNamelink(String namelink);

}