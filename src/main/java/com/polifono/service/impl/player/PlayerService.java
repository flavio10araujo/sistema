package com.polifono.service.impl.player;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.common.properties.ConfigsCreditsProperties;
import com.polifono.common.util.PasswordUtil;
import com.polifono.model.entity.Player;
import com.polifono.model.enums.Role;
import com.polifono.repository.IPlayerRepository;
import com.polifono.service.impl.GenerateRandomStringService;
import com.polifono.service.impl.SendEmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerService {

    private final ConfigsCreditsProperties configsCreditsProperties;
    private final GenerateRandomStringService generateRandomStringService;
    private final IPlayerRepository repository;
    private final SendEmailService emailSendUtil;

    public Player save(Player player) {
        return repository.save(player);
    }

    public Optional<Player> findById(int id) {
        return repository.findById(id);
    }

    public List<Player> findAll() {
        return repository.findAll();
    }

    public Optional<Player> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<Player> findByEmailAndStatus(String email, boolean status) {
        return repository.findByEmailAndActive(email, status);
    }

    public Optional<Player> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public Optional<Player> findByIdFacebook(Long id) {
        return repository.findByIdFacebook(id);
    }

    public List<Player> findByDateIncRange(Date dateBegin, Date dateEnd) {
        return repository.findByDateIncRange(dateBegin, dateEnd);
    }

    /**
     * This method is used to log in.
     * This method was changed to permit that a user without email could access the system.
     */
    public Optional<Player> findByEmailAndStatusForLogin(String email, boolean status) {
        log.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));

        Optional<Player> byEmail = repository.findByEmailAndStatusForLogin(email, status);

        if (byEmail.isPresent()) {
            return byEmail;
        } else {
            // In this case:
            // - Or the email doesn't exist;
            // - Or the player is trying to access the system with his login.
            return repository.findByLoginAndStatusForLogin(email, status);
        }
    }

    public Player create(Player player) {
        return repository.save(preparePlayerForCreation(player));
    }

    /**
     * Verify if the player has already confirmed his e-mail.
     * Return true if the player has already confirmed it. Return false otherwise.
     */
    public boolean isEmailConfirmed(Player player) {
        return findById(player.getId())
                .map(Player::isIndEmailConfirmed)
                .orElse(false);
    }

    private Player preparePlayerForCreation(Player player) {
        player.setDtInc(new Date());
        player.setActive(true);
        player.setPassword(PasswordUtil.encryptPassword(player.getPassword()));
        player.setCredit(configsCreditsProperties.getCreation()); // n credits are given to the player when he creates the account.
        player.setRole(Role.USER);
        player.setEmailConfirmed(generateRandomStringService.generate(10)); // This field is sent to the player's email to confirm if the email is real.
        return player;
    }

    public void confirmEmail(Player player) {
        player.setIndEmailConfirmed(true);
        save(player);
    }

    public void resendEmailConfirmation(Player player) {
        player.setEmailConfirmed(generateRandomStringService.generate(10));
        save(player);
        emailSendUtil.sendEmailConfirmRegister(player);
    }
}
