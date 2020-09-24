package com.polifono.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerPromo;
import com.polifono.repository.IPlayerPromoRepository;
import com.polifono.service.IPlayerPromoService;

@Service
public class PlayerPromoServiceImpl implements IPlayerPromoService {

	private IPlayerPromoRepository repository;
	
	@Autowired
	public PlayerPromoServiceImpl(IPlayerPromoRepository repository) {
		this.repository = repository;
	}
	
	public final PlayerPromo save(PlayerPromo playerPromo) {
		return repository.save(playerPromo);
	}
	
	@Override
	public PlayerPromo findByPlayerAndPromo(int playerId, int promoId) {
		return repository.findByPlayerAndPromo(playerId, promoId);
	}
}