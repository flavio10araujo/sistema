package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Answer;

public interface IAnswerService {

    Answer save(Answer o);

    Boolean delete(Integer id);

    Optional<Answer> findById(int id);

    List<Answer> findAll();

    List<Answer> findAllByGameId(int gameId);

    List<Answer> findAllByGameIdAndLevelId(int gameId, int levelId);

    List<Answer> findAllByMapId(int mapId);

    List<Answer> findAllByPhaseId(int phaseId);

    List<Answer> findAllByQuestionId(int questionId);
}
