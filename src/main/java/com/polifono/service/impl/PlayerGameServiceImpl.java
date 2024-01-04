package com.polifono.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;
import com.polifono.service.IPlayerGameService;

@Service
public class PlayerGameServiceImpl implements IPlayerGameService {

    private IPlayerGameRepository repository;

    @Autowired
    public PlayerGameServiceImpl(IPlayerGameRepository repository) {
        this.repository = repository;
    }

    public final PlayerGame save(PlayerGame playerGame) {
        return repository.save(playerGame);
    }

    public final Optional<PlayerGame> findById(int playerGameId) {
        return repository.findById(playerGameId);
    }

    public final PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits) {
        return repository.save(preparePlayerGameForRemovingCredits(playerGame, qtdCredits));
    }

    public PlayerGame preparePlayerGameForRemovingCredits(PlayerGame playerGame, int qtdCredits) {
        playerGame.setCredit(playerGame.getCredit() - qtdCredits);
        return playerGame;
    }

    public final PlayerGame findByPlayerAndGame(int playerId, int gameId) {
        return repository.findByPlayerAndGame(playerId, gameId);
    }
}
