package com.polifono.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;

public interface IPlayerService {

    Player save(Player o);

    Optional<Player> findById(int id);

    List<Player> findAll();

    Player findByEmail(String email);

    Player findByEmailAndStatus(String email, boolean status);

    Player findByLogin(String login);

    Player findByIdFacebook(Long id);

    List<Player> findByDateIncRange(Date dateBegin, Date dateEnd);

    Player create(Player o);

    Optional<Player> findByEmailAndStatusForLogin(String email, boolean status);

    Player addCreditsToPlayer(int playerId, int qtdCredits);

    Player removeCreditsFromPlayer(int playerId, int qtdCredits);

    Player removeOneCreditFromPlayer(Player player, Game game);

    boolean playerHasCredits(Player user, Phase phase);

    boolean isEmailConfirmed(Player user);

    String validateCreatePlayer(Player player);

    String validateCreatePlayerByTeacher(Player player);

    String validateUpdateProfile(Player edit);

    String validateChangePasswordPlayer(Player player);

    String validateAddVideo(Playervideo playervideo);

    List<Player> findCommunicationGroup04();

    List<Player> findCommunicationGroup05();
}
