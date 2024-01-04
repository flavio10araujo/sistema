package com.polifono.service;

import java.util.Optional;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameService {

    public PlayerGame save(PlayerGame o);

    public Optional<PlayerGame> findById(int id);

    public PlayerGame findByPlayerAndGame(int playerId, int gameId);

    public PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits);
}
