package com.polifono.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;

public interface IPlayerService {

	public Player create(Player player);
	
	public Player save(Player player);
	
	public Player findOne(int id);
	
	public List<Player> findAll();
	
	public Player findByEmail(String email);
	
	public Player findByEmailAndStatus(String email, boolean status);
	
	public Optional<Player> findByEmailAndStatusForLogin(String email, boolean status);
	
	public String encryptPassword(@Nonnull final String rawPassword);
	
	public Player addCreditsToPlayer(int playerId, int qtdCredits);
	
	public Player removeCreditsFromPlayer(int playerId, int qtdCredits);

	public Player removeOneCreditFromPlayer(Player player, Game game);

	public boolean playerHasCredits(Player user, Phase phase);

	public boolean isEmailConfirmed(Player user);

	public String validateCreatePlayer(Player player);
	
	public String validateCreatePlayerByTeacher(Player player);

	public String validateUpdateProfile(Player edit);
	
	public Player findByLogin(String login);

}