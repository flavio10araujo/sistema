package com.polifono.service;

import com.polifono.domain.PlayerPromo;

public interface IPlayerPromoService {
	
	public PlayerPromo save(PlayerPromo o);
	
	//public Boolean delete(Integer id);
	
	//public PlayerPhase findOne(int id);
		
	//public List<PlayerPhase> findAll();
	
	
	//public List<PlayerPromo> findByPlayer(int playerId);
	
	public PlayerPromo findByPlayerAndPromo(int playerId, int promoId);
}