package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Answer;

public interface IAnswerService {

    Answer save(Answer o);

    Boolean delete(Integer id);

    Optional<Answer> findById(int id);

    List<Answer> findAll();

    List<Answer> findByGame(int gameId);

    List<Answer> findByGameAndLevel(int gameId, int levelId);

    List<Answer> findByMap(int mapId);

    List<Answer> findByPhase(int phaseId);

    List<Answer> findByQuestion(int questionId);
}
