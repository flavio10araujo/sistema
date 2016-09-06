package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Level;
import com.polifono.repository.ILevelRepository;
import com.polifono.service.ILevelService;

@Service
public class LevelServiceImpl implements ILevelService {

	@Autowired
	private ILevelRepository levelRepository;
	
	public final List<Level> findAll() {
		return (List<Level>) levelRepository.findAll();
	}
	
	public List<Level> findByGame(int gameId) {
		return (List<Level>) levelRepository.findByGame(gameId);
	}
}