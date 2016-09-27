package com.polifono.service;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameService {

	public PlayerGame findOne(int playerGameId);
	
	public PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits);

}