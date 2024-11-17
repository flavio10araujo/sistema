package com.polifono.model.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayerTest {

    public static final String DT_BIRTHDAY = "12/18/1985";

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void givenBirthdayDate_whenGetDtBirthDay_thenReturnCorrectDay() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        player.setDtBirth(sdf.parse(DT_BIRTHDAY));
        Assertions.assertEquals(18, player.getDtBirthDay());
    }

    @Test
    public void givenBirthdayDate_whenGetDtBirthMonth_thenReturnCorrectMonth() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        player.setDtBirth(sdf.parse(DT_BIRTHDAY));
        Assertions.assertEquals(11, player.getDtBirthMonth());
    }

    @Test
    public void givenBirthdayDate_whenGetDtBirthYear_thenReturnCorrectYear() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        player.setDtBirth(sdf.parse(DT_BIRTHDAY));
        Assertions.assertEquals(1985, player.getDtBirthYear());
    }

    @Test
    public void givenPlayer_whenPlayerHasNoCredits_thenReturnCorrectTotalCredit() {
        player.setCredit(0);
        Assertions.assertEquals(0, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_whenPlayerHasNormalCredits_thenReturnCorrectTotalCredit() {
        player.setCredit(10);
        Assertions.assertEquals(10, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_whenPlayerHasSpecificCredits_thenReturnCorrectTotalCredit() {
        player.setPlayerGameList(List.of(createPlayerGameWithCredit(20)));
        Assertions.assertEquals(20, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_whenPlayerHasSpecificCreditsInMoreThanOneGame_thenReturnCorrectTotalCredit() {
        player.setPlayerGameList(List.of(createPlayerGameWithCredit(20), createPlayerGameWithCredit(30)));
        Assertions.assertEquals(50, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_whenPlayerHasNormalAndSpecificCredits_thenReturnCorrectTotalCredit() {
        player.setCredit(10);
        player.setPlayerGameList(List.of(createPlayerGameWithCredit(20)));
        Assertions.assertEquals(30, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_whenGetFullName_thenReturnCorrectFullName() {
        player.setName("John");
        player.setLastName("Doe");
        Assertions.assertEquals("John Doe", player.getFullName());
    }

    private PlayerGame createPlayerGameWithCredit(int credit) {
        PlayerGame playerGame = new PlayerGame();
        playerGame.setCredit(credit);
        return playerGame;
    }
}
