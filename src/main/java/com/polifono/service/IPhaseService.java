package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;

public interface IPhaseService {

    public Phase save(Phase o);

    public Boolean delete(Integer id);

    public Optional<Phase> findById(int id);

    public List<Phase> findAll();

    public List<Phase> findByGame(int gameId);

    public List<Phase> findByGameAndLevel(int gameId, int levelId);

    public List<Phase> findByMap(int mapId);

    public Phase findByMapAndOrder(int mapId, int phaseOrder);

    public Phase findNextPhaseInThisMap(int mapId, int phaseOrder);

    public Phase findLastPhaseDoneByPlayerAndGame(int playerId, int gameId);

    public Phase findLastPhaseOfTheLevel(int gameId, int levelId);

    public List<Phase> findGamesForProfile(int playerId);

    public List<Phase> findPhasesCheckedByMap(Map map, PlayerPhase lastPhaseCompleted);

    public boolean playerCanAccessThisPhase(Phase phase, Player user);

    public List<Phase> findPhasesBySearchAndUser(String q, int playerId);
}
