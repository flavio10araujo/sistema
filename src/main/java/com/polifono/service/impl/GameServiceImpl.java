package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.repository.IGameRepository;
import com.polifono.service.IGameService;

@Service
public class GameServiceImpl implements IGameService {

	@Autowired
	private IGameRepository repository;

	public final List<Game> findAll() {
		return (List<Game>) repository.findAll();
	}
	
	public final Game findByNamelink(String namelink) {
		return repository.findByNamelink(namelink);
	}
}