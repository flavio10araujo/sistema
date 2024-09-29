package com.polifono.domain;

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
    public void givenBirthdayDate_WhenGetDtBirthDay_ThenReturnCorrectDay() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        player.setDtBirth(sdf.parse(DT_BIRTHDAY));
        Assertions.assertEquals(18, player.getDtBirthDay());
    }

    @Test
    public void givenBirthdayDate_WhenGetDtBirthMonth_ThenReturnCorrectMonth() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        player.setDtBirth(sdf.parse(DT_BIRTHDAY));
        Assertions.assertEquals(11, player.getDtBirthMonth());
    }

    @Test
    public void givenBirthdayDate_WhenGetDtBirthYear_ThenReturnCorrectYear() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        player.setDtBirth(sdf.parse(DT_BIRTHDAY));
        Assertions.assertEquals(1985, player.getDtBirthYear());
    }

    @Test
    public void givenPlayer_WhenPlayerHasNoCredits_ThenReturnCorrectTotalCredit() {
        player.setCredit(0);
        Assertions.assertEquals(0, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_WhenPlayerHasNormalCredits_ThenReturnCorrectTotalCredit() {
        player.setCredit(10);
        Assertions.assertEquals(10, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_WhenPlayerHasSpecificCredits_ThenReturnCorrectTotalCredit() {
        player.setPlayerGameList(List.of(createPlayerGameWithCredit(20)));
        Assertions.assertEquals(20, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_WhenPlayerHasSpecificCreditsInMoreThanOneGame_ThenReturnCorrectTotalCredit() {
        player.setPlayerGameList(List.of(createPlayerGameWithCredit(20), createPlayerGameWithCredit(30)));
        Assertions.assertEquals(50, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_WhenPlayerHasNormalAndSpecificCredits_ThenReturnCorrectTotalCredit() {
        player.setCredit(10);
        player.setPlayerGameList(List.of(createPlayerGameWithCredit(20)));
        Assertions.assertEquals(30, player.getTotalCredit());
    }

    @Test
    public void givenPlayer_WhenGetFullName_ThenReturnCorrectFullName() {
        player.setName("John");
        player.setLastName("Doe");
        Assertions.assertEquals("John Doe", player.getFullName());
    }

    @Test
    public void givenPlayer_WhenGetEmailMD5_ThenReturnCorrectMD5() {
        player.setEmail("flavio@email.com");
        Assertions.assertEquals("4c0cd4d99b6086f9174315bbfbb103e9", player.getEmailMD5());
    }

    @Test
    public void givenPlayer_WhenGetRankColor_ThenReturnCorrectColor() {
        player.setScore(0);
        Assertions.assertEquals("Faixa Branca", player.getRankColor());
        player.setScore(1000);
        Assertions.assertEquals("Faixa Branca", player.getRankColor());
        player.setScore(1001);
        Assertions.assertEquals("Faixa Amarela", player.getRankColor());
        player.setScore(3001);
        Assertions.assertEquals("Faixa Laranjada", player.getRankColor());
        player.setScore(5001);
        Assertions.assertEquals("Faixa Vermelha", player.getRankColor());
        player.setScore(6501);
        Assertions.assertEquals("Faixa Roxa", player.getRankColor());
        player.setScore(9001);
        Assertions.assertEquals("Faixa Marrom", player.getRankColor());
        player.setScore(14001);
        Assertions.assertEquals("Faixa Preta", player.getRankColor());
        player.setScore(18001);
        Assertions.assertEquals("Faixa Cobre", player.getRankColor());
        player.setScore(21001);
        Assertions.assertEquals("Faixa Prateada", player.getRankColor());
        player.setScore(24001);
        Assertions.assertEquals("Faixa Dourada", player.getRankColor());
    }

    @Test
    public void givenPlayer_WhenGetRankLevel_ThenReturnCorrectLevel() {
        player.setScore(0);
        Assertions.assertEquals(1, player.getRank().getLevel());
        player.setScore(1000);
        Assertions.assertEquals(1, player.getRank().getLevel());
        player.setScore(1001);
        Assertions.assertEquals(2, player.getRank().getLevel());
        player.setScore(3001);
        Assertions.assertEquals(3, player.getRank().getLevel());
        player.setScore(5001);
        Assertions.assertEquals(4, player.getRank().getLevel());
        player.setScore(6501);
        Assertions.assertEquals(5, player.getRank().getLevel());
        player.setScore(9001);
        Assertions.assertEquals(6, player.getRank().getLevel());
        player.setScore(14001);
        Assertions.assertEquals(7, player.getRank().getLevel());
        player.setScore(18001);
        Assertions.assertEquals(8, player.getRank().getLevel());
        player.setScore(21001);
        Assertions.assertEquals(9, player.getRank().getLevel());
        player.setScore(24001);
        Assertions.assertEquals(10, player.getRank().getLevel());
    }

    private PlayerGame createPlayerGameWithCredit(int credit) {
        PlayerGame playerGame = new PlayerGame();
        playerGame.setCredit(credit);
        return playerGame;
    }
}
