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
	
	/**
	 * This method is used to calculate the correct score to the player.
	 * 
	 * @param numAttempts
	 * @param grade
	 * @return
	 */
	public int calculateScore(int numAttempts, int grade) {
		/*
		 1
		 100 = 100
		 90 = 90
		 80 = 80
		 70 = 70
		 2
		 100 = 70 
		 90 = 65
		 80 = 60
		 70 = 50
		 3
		 100 = 50
		 90 = 45
		 80 = 40
		 70 = 30
		 4...
		 10 
		 */
		
		if (numAttempts == 1) {
			return grade;
		}
		
		if (numAttempts == 2) {
			if (grade > 90) {
				return 70;
			}
			if (grade > 80) {
				return 65;
			}
			if (grade > 70) {
				return 60;
			}
			if (grade == 70) {
				return 50;
			}
		}
		
		if (numAttempts == 3) {
			if (grade > 90) {
				return 50;
			}
			if (grade > 80) {
				return 45;
			}
			if (grade > 70) {
				return 40;
			}
			if (grade == 70) {
				return 30;
			}
		}
		
		return 10;
	}
}