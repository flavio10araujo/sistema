package com.polifono.service;

import com.polifono.domain.PlayerPromo;

public interface IPlayerPromoService {

    PlayerPromo save(PlayerPromo o);

    PlayerPromo findByPlayerAndPromo(int playerId, int promoId);
}
