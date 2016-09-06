package com.polifono.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;

@Service
public class PlayerGameServiceImpl {

	@Autowired
	private IPlayerGameRepository playerGameRepository;
	
	public final PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits) {
		playerGame.setCredit(playerGame.getCredit() - qtdCredits);
		return playerGameRepository.save(playerGame);
	}
}