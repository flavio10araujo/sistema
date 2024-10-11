package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Question;

public interface IQuestionService {

    Question save(Question o);

    Boolean delete(Integer id);

    Optional<Question> findById(int id);

    List<Question> findAll();

    List<Question> findByGame(int gameId);

    List<Question> findByGameAndLevel(int gameId, int levelId);

    List<Question> findByMap(int mapId);

    List<Question> findByPhase(int phaseId);

    List<Question> findByContent(int contentId);
}
