package com.polifono.service;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameService {

	public PlayerGame save(PlayerGame o);
	
	//public Boolean delete(Integer id);
	
	public PlayerGame findOne(int id);
	
	//public List<PlayerGame> findAll();
	
	
	public PlayerGame findByPlayerAndGame(int playerId, int gameId);
	
	
	public PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits);

}