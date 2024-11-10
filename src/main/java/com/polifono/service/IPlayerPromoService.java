package com.polifono.service;

import com.polifono.model.entity.PlayerPromo;

public interface IPlayerPromoService {

    PlayerPromo save(PlayerPromo o);

    PlayerPromo findByPlayerAndPromo(int playerId, int promoId);
}
