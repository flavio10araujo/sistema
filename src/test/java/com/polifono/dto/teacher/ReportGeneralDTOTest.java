package com.polifono.dto.teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;

public class ReportGeneralDTOTest {

    @Test
    public void givenPlayerPhases_whenGetScore_thenReturnSumOfScores() {
        Player player = new Player();
        PlayerPhase phase1 = new PlayerPhase();
        phase1.setScore(10);
        PlayerPhase phase2 = new PlayerPhase();
        phase2.setScore(20);
        List<PlayerPhase> playerPhases = Arrays.asList(phase1, phase2);

        ReportGeneralDTO report = new ReportGeneralDTO(1, 2, player, playerPhases);
        int score = report.getScore();

        assertEquals(30, score);
    }

    @Test
    public void givenNoPlayerPhases_whenGetScore_thenReturnZero() {
        Player player = new Player();
        List<PlayerPhase> playerPhases = List.of();

        ReportGeneralDTO report = new ReportGeneralDTO(1, 2, player, playerPhases);
        int score = report.getScore();

        assertEquals(0, score);
    }

    @Test
    public void givenPlayerPhases_whenGetAverage_thenReturnCorrectAverage() {
        Player player = new Player();
        PlayerPhase phase1 = new PlayerPhase();
        phase1.setScore(10);
        PlayerPhase phase2 = new PlayerPhase();
        phase2.setScore(20);
        List<PlayerPhase> playerPhases = Arrays.asList(phase1, phase2);

        ReportGeneralDTO report = new ReportGeneralDTO(1, 2, player, playerPhases);
        double average = report.getAverage();

        assertEquals(15.0, average);
    }

    @Test
    public void givenNoPlayerPhases_whenGetAverage_thenReturnZero() {
        Player player = new Player();
        List<PlayerPhase> playerPhases = List.of();

        ReportGeneralDTO report = new ReportGeneralDTO(1, 2, player, playerPhases);
        double average = report.getAverage();

        assertEquals(0.0, average);
    }
}
