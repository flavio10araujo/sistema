package com.polifono.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.PlayerPromo;

public interface IPlayerPromoRepository extends JpaRepository<PlayerPromo, UUID> {

    @Query("SELECT playerPromo FROM PlayerPromo playerPromo WHERE playerPromo.player.id = :playerId AND playerPromo.promo.id = :promoId")
    PlayerPromo findByPlayerAndPromo(@Param("playerId") int playerId, @Param("promoId") int promoId);
}
