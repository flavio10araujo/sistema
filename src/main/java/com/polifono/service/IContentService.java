package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Content;

public interface IContentService {

    Content save(Content o);

    boolean delete(Integer id);

    Optional<Content> findById(int id);

    List<Content> findAll();

    List<Content> findAllText();

    List<Content> findContentsTextByGame(int gameId);

    List<Content> findContentsTextByGameAndLevel(int gameId, int levelId);

    List<Content> findContentsTextByMap(int mapId);

    List<Content> findContentsTextByPhase(int phaseId);

    List<Content> findAllTest();

    List<Content> findContentsTestByGame(int gameId);

    List<Content> findContentsTestByGameAndLevel(int gameId, int levelId);

    List<Content> findContentsTestByMap(int mapId);

    List<Content> findContentsTestByPhase(int phaseId);

    Content findByPhaseAndOrder(int phaseId, int contentOrder);
}
