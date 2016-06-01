package com.polifono.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Level;
import com.polifono.repository.LevelRepository;

@Service
public class LevelService {

	private final LevelRepository levelRepository;
	
	@Autowired
	public LevelService(final LevelRepository levelRepository) {
		this.levelRepository = levelRepository;
	}
	
	public final List<Level> findAll() {
		return (List<Level>) levelRepository.findAll();
	}
	
	public List<Level> findByGame(int gameId) {
		return (List<Level>) levelRepository.findByGame(gameId);
	}
}