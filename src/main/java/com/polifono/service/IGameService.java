package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Phase;

public interface IGameService {

    List<Game> findAll();

    List<Game> findByActive(boolean active);

    Optional<Game> findByNamelink(String namelink);

    int calculateScore(int numAttempts, int grade);

    int calculateGrade(List<Integer> questionsId, java.util.Map<String, String> playerAnswers);

    Phase getPhaseOfTheTest(List<Integer> questionsId);
}
