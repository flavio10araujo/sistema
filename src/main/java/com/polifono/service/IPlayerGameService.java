package com.polifono.service;

import java.util.Optional;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameService {

    PlayerGame save(PlayerGame o);

    Optional<PlayerGame> findById(int id);

    PlayerGame findByPlayerAndGame(int playerId, int gameId);

    PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits);
}
