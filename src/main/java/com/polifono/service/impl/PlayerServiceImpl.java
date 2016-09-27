package com.polifono.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.polifono.domain.Player;
import com.polifono.domain.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.IPlayerService;
import com.polifono.util.RandomStringUtil;

@Service
public class PlayerServiceImpl implements IPlayerService {

	@Autowired
	private IPlayerRepository repository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerServiceImpl.class);
	
	private static ResourceBundle resourceBundle;
	
	static {
		 resourceBundle = ResourceBundle.getBundle("application", Locale.getDefault());
	}
	
	public final Player create(Player player) {
		player.setDtInc(new Date());
		player.setActive(true);
		player.setPassword(encryptPassword(player.getPassword()));
		player.setCredit(Integer.parseInt(resourceBundle.getString("credits.creation"))); // n credits are given to the player when he creates the account.
		player.setRole(Role.USER);
		player.setEmailConfirmed(new RandomStringUtil(10).nextString()); // This field is sent to the player's email to confirm if the email is real.
		return repository.save(player);
	}
	
	public final Player save(Player player) {
		return repository.save(player);
	}
	
	public Player findOne(int id) {
        return repository.findOne(id);
    }
	
	public final List<Player> findAll() {
		return (List<Player>) repository.findAll();
	}
	
	public Player findByEmail(String email) {
        return repository.findByEmail(email);
    }
	
	public Player findByEmailAndStatus(String email, boolean status) {
        return repository.findByEmailAndStatus(email, status);
    }
	
	/**
	 * This method is used to login.
	 * 
	 * @param email
	 * @return
	 */
	public Optional<Player> findByEmailAndStatusForLogin(String email, boolean status) {
        LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));
        return repository.findByEmailAndStatusForLogin(email, status);
    }
	
	public String encryptPassword(@Nonnull final String rawPassword) {
        final String salt = BCrypt.gensalt(10, new SecureRandom());
        return BCrypt.hashpw(rawPassword, salt);
    }
	
	public final Player addCreditsToPlayer(int playerId, int qtdCredits) {
		Player player = findOne(playerId);
		player.setCredit(player.getCredit() + qtdCredits);
		return repository.save(player);
	}
	
	public final Player removeCreditsFromPlayer(int playerId, int qtdCredits) {
		Player player = findOne(playerId);
		player.setCredit(player.getCredit() - qtdCredits);
		return repository.save(player);
	}
}