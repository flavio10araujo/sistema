package com.polifono.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerGame;
import com.polifono.repository.PlayerGameRepository;

@Service
public class PlayerGameService {

	@Autowired
	private PlayerGameRepository playerGameRepository;
	
	public final PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits) {
		playerGame.setCredit(playerGame.getCredit() - qtdCredits);
		return playerGameRepository.save(playerGame);
	}
}