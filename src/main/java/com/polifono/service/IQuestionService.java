package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Question;

public interface IQuestionService {

    public Question save(Question o);

    public Boolean delete(Integer id);

    public Optional<Question> findById(int id);

    public List<Question> findAll();

    public List<Question> findByGame(int gameId);

    public List<Question> findByGameAndLevel(int gameId, int levelId);

    public List<Question> findByMap(int mapId);

    public List<Question> findByPhase(int phaseId);

    public List<Question> findByContent(int contentId);
}
