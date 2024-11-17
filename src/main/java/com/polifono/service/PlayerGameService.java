package com.polifono.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.PlayerGame;
import com.polifono.repository.IPlayerGameRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerGameService {

    private final IPlayerGameRepository repository;

    public PlayerGame save(PlayerGame playerGame) {
        return repository.save(playerGame);
    }

    public Optional<PlayerGame> findById(int playerGameId) {
        return repository.findById(playerGameId);
    }

    public PlayerGame removeCreditsFromPlayer(PlayerGame playerGame, int qtdCredits) {
        return repository.save(preparePlayerGameForRemovingCredits(playerGame, qtdCredits));
    }

    public PlayerGame preparePlayerGameForRemovingCredits(PlayerGame playerGame, int qtdCredits) {
        playerGame.setCredit(playerGame.getCredit() - qtdCredits);
        return playerGame;
    }

    public PlayerGame findByPlayerAndGame(int playerId, int gameId) {
        return repository.findByPlayerAndGame(playerId, gameId);
    }
}
