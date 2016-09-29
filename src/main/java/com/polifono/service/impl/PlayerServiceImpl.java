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

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerGame;
import com.polifono.domain.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.IPlayerGameService;
import com.polifono.service.IPlayerService;
import com.polifono.util.RandomStringUtil;

@Service
public class PlayerServiceImpl implements IPlayerService {

	@Autowired
	private IPlayerRepository repository;
	
	@Autowired
    private IPlayerGameService playerGameService;
	
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
        
        System.out.println("EMAIL2: " + email + " status: " + status);
        
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
	
	/**
	 * Remove credits from an user.
	 * Analyze if the player has specific credits of the game passed.
	 * In case affirmative, remove the specific credit from the game.
	 * Otherwise, remove a general credit.  
	 * 
	 * @param player
	 * @param game
	 * @return
	 */
	public Player removeOneCreditFromPlayer(Player player, Game game) {
		boolean hasSpecificCredits = false;
		PlayerGame playerGame = null;
		
		label : {
			for (PlayerGame pg : player.getPlayerGameList()) {
				// If the player has specific credits of this game.
				if ((game.getId() == pg.getGame().getId()) && (pg.getCredit() > 0)) {
					hasSpecificCredits = true;
					playerGame = pg;
					break label;
				}
			}
		}
		
		if (hasSpecificCredits) {
			playerGameService.removeCreditsFromPlayer(playerGame, 1);
			return this.findOne(player.getId());
		}
		else {
			return this.removeCreditsFromPlayer(player.getId(), 1);
		}
	}
	
	/**
	 * Verify if the player has enough credits to play the phase.
	 * Return true, if the player has credits.
	 * This method verify the generic credits and the specific credits for the game passed.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean playerHasCredits(Player player, Phase phase) {
		// If the player doesn't have credits anymore.
		player = this.findOne(player.getId());
		boolean hasCredits = false;
		
		// If the player has generic credits.
		if (player.getCredit() > 0) {
			hasCredits = true;
		}
		else {
			label : {
				// If the player doesn't have generic credits. Let's see if the player has specific credits.
				for (PlayerGame pg : player.getPlayerGameList()) {
					// If the player has specific credits of this game.
					if ((phase.getMap().getGame().getId() == pg.getGame().getId()) && (pg.getCredit() > 0)) {
						hasCredits = true;
						break label;
					}
				}
			}
		}
		
		return hasCredits;
	}
}