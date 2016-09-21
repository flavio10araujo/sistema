package com.polifono.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;
import com.polifono.service.IPlayerGameService;

@Service
public class PlayerGameServiceImpl implements IPlayerGameService {

	@Autowired
	private IPlayerGameRepository repository;
	
	public final PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits) {
		playerGame.setCredit(playerGame.getCredit() - qtdCredits);
		return repository.save(playerGame);
	}
}