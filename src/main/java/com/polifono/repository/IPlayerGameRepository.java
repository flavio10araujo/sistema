package com.polifono.repository;

import org.springframework.data.repository.CrudRepository;

import com.polifono.domain.PlayerGame;

public interface IPlayerGameRepository extends CrudRepository<PlayerGame, Integer> {

	
}