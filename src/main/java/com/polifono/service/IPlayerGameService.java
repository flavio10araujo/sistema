package com.polifono.service;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameService {

	public PlayerGame save(PlayerGame playerGame);
	
	public PlayerGame findOne(int playerGameId);
	
	public PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits);
	
	public PlayerGame findByPlayerAndGame(int playerId, int gameId);

}