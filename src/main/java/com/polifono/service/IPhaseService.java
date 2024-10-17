package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.PlayerPhase;

public interface IPhaseService {

    Phase save(Phase o);

    boolean delete(Integer id);

    Optional<Phase> findById(int id);

    List<Phase> findAll();

    List<Phase> findByGame(int gameId);

    List<Phase> findByGameAndLevel(int gameId, int levelId);

    List<Phase> findByMap(int mapId);

    Optional<Phase> findByMapAndOrder(int mapId, int phaseOrder);

    Optional<Phase> findNextPhaseInThisMap(int mapId, int phaseOrder);

    Optional<Phase> findLastPhaseDoneByPlayerAndGame(int playerId, int gameId);

    Optional<Phase> findLastPhaseOfTheLevel(int gameId, int levelId);

    List<Phase> findGamesForProfile(int playerId);

    List<Phase> findPhasesCheckedByMap(Map map, PlayerPhase lastPhaseCompleted);

    boolean canPlayerAccessPhase(Phase phase, int playerId);

    List<Phase> findPhasesBySearchAndUser(String q, int playerId);
}
