package com.polifono.service.impl;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.PlayerPromo;
import com.polifono.repository.IPlayerPromoRepository;
import com.polifono.service.IPlayerPromoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerPromoServiceImpl implements IPlayerPromoService {

    private final IPlayerPromoRepository repository;

    public PlayerPromo save(PlayerPromo playerPromo) {
        return repository.save(playerPromo);
    }

    @Override
    public PlayerPromo findByPlayerAndPromo(int playerId, int promoId) {
        return repository.findByPlayerAndPromo(playerId, promoId);
    }
}
