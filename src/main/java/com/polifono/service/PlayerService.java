package com.polifono.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.polifono.domain.Player;
import com.polifono.domain.Role;
import com.polifono.repository.PlayerRepository;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);
	
	public final Player save(Player player) {
		return playerRepository.save(player);
	}
	
	public Player getPlayer(int id) {
        return playerRepository.findOne(id);
    }
	
	public Player getPlayerByEmail(String email) {
        return playerRepository.findPlayerByEmail(email);
    }
	
	public final Player createPlayer(Player player) {
		player.setDtInc(new Date());
		player.setPassword(encryptPassword(player.getPassword()));
		player.setCredit(3); // 3 credits are given to the player when he creates the account.
		player.setRole(Role.USER);
		return playerRepository.save(player);
	}
	
	public final Player addCreditsToPlayer(int playerId, int qtdCredits) {
		Player player = getPlayer(playerId);
		player.setCredit(player.getCredit() + qtdCredits);
		return playerRepository.save(player);
	}
	
	public final List<Player> findAll() {
		List<Player> lista = (List<Player>) playerRepository.findAll();
		return lista;
	}
	
	public String encryptPassword(@Nonnull final String rawPassword) {
        final String salt = BCrypt.gensalt(10, new SecureRandom());
        return BCrypt.hashpw(rawPassword, salt);
    }
	
	/**
	 * This method is used to login.
	 * 
	 * @param email
	 * @return
	 */
	public Optional<Player> getUserByEmail(String email) {
        LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));
        return playerRepository.findUserByEmail(email);
    }
}