package com.polifono.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.polifono.domain.Player;

public interface IPlayerService {

	public Player create(Player player);
	
	public Player save(Player player);
	
	public Player find(int id);
	
	public List<Player> findAll();
	
	public Player findByEmail(String email);
	
	public Player findByEmailAndStatus(String email, boolean status);
	
	public Optional<Player> findByEmailAndStatusForLogin(String email, boolean status);
	
	public String encryptPassword(@Nonnull final String rawPassword);
	
	public Player addCreditsToPlayer(int playerId, int qtdCredits);
	
	public Player removeCreditsFromPlayer(int playerId, int qtdCredits);

}