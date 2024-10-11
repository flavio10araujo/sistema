package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;

public interface IGameService {

    List<Game> findAll();

    List<Game> findByActive(boolean active);

    Game findByNamelink(String namelink);

    int calculateScore(int numAttempts, int grade);

    int calculateGrade(List<Integer> questionsId, java.util.Map<String, String> playerAnswers);

    Phase getPhaseOfTheTest(List<Integer> questionsId);
}
