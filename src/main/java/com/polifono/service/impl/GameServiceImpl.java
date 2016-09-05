package com.polifono.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.repository.GameRepository;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;

	public final List<Game> findAll() {
		return (List<Game>) gameRepository.findAll();
	}
	
	public final Game findByNamelink(String namelink) {
		Game game = gameRepository.findByNamelink(namelink); 
		return game;
	}
}