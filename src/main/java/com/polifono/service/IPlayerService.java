package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;

public interface IPlayerService {

	public Player save(Player o);
	
	//public Boolean delete(Integer id);
	
	public Player findOne(int id);
	
	public List<Player> findAll();
	
	
	public Player findByEmail(String email);
	
	public Player findByEmailAndStatus(String email, boolean status);
	
	public Player findByLogin(String login);
	
	public Player findByIdFacebook(Long id);
	
	
	public Player create(Player o);
	
	public Optional<Player> findByEmailAndStatusForLogin(String email, boolean status);
	
	public Player addCreditsToPlayer(int playerId, int qtdCredits);
	
	public Player removeCreditsFromPlayer(int playerId, int qtdCredits);

	public Player removeOneCreditFromPlayer(Player player, Game game);

	public boolean playerHasCredits(Player user, Phase phase);

	public boolean isEmailConfirmed(Player user);

	public String validateCreatePlayer(Player player);
	
	public String validateCreatePlayerByTeacher(Player player);

	public String validateUpdateProfile(Player edit);
	
	public String validateChangePasswordPlayer(Player player);

}