package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.repository.IMapRepository;
import com.polifono.service.IPhaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MapService {

    private final IMapRepository repository;
    private final IPhaseService phaseService;

    public Map save(Map map) {
        return repository.save(map);
    }

    public boolean delete(Integer id) {
        Optional<Map> temp = repository.findById(id);

        if (temp.isPresent()) {
            try {
                repository.delete(temp.get());
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        return false;
    }

    public Optional<Map> findById(int id) {
        return repository.findById(id);
    }

    public List<Map> findAll() {
        return repository.findAll();
    }

    public List<Map> findMapsByGame(int gameId) {
        return repository.findMapsByGame(gameId);
    }

    public List<Map> findMapsByGameAndLevel(int gameId, int levelId) {
        return repository.findMapsByGameAndLevel(gameId, levelId);
    }

    public Optional<Map> findByGameAndLevel(int gameId, int levelId) {
        List<Map> maps = repository.findMapsByGameAndLevel(gameId, levelId);

        if (!maps.isEmpty()) {
            return Optional.of(maps.get(0));
        }

        return Optional.empty();
    }

    public Optional<Map> findByGameLevelAndOrder(int gameId, int levelId, int mapOrder) {
        List<Map> maps = repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder);

        if (!maps.isEmpty()) {
            return Optional.of(maps.get(0));
        }

        return Optional.empty();
    }

    public Optional<Map> findNextMapSameLevel(Map mapCurrent) {
        return repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder());
    }

    /**
     * Verify if the player has permission to access a specific map.
     * Return true if the player has the permission.
     */
    public boolean canPlayerAccessMap(Map map, int playerId) {
        if (isFirstMapOfFirstLevel(map)) {
            return true;
        }

        Optional<Phase> lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(playerId, map.getGame().getId());
        if (lastPhaseDone.isEmpty()) {
            return false;
        }

        if (isPreviousLevel(map, lastPhaseDone.get()) || isSameOrPreviousMapInSameLevel(map, lastPhaseDone.get())) {
            return true;
        }

        Optional<Phase> lastPhaseOfTheLevel = phaseService.findLastPhaseOfTheLevel(lastPhaseDone.get().getMap().getGame().getId(),
                lastPhaseDone.get().getMap().getLevel().getId());
        if (lastPhaseOfTheLevel.isEmpty() || !isLastPhaseOfLevel(lastPhaseDone.get(), lastPhaseOfTheLevel.get())) {
            return false;
        }

        return isNextMapInSameLevel(map, lastPhaseDone.get()) || isFirstMapOfNextLevel(map, lastPhaseDone.get());
    }

    /**
     * Get the current map based on the last phase completed by the player in a specific game.
     * If the map is the last map of the level, it returns with the flag levelCompleted true.
     * If the map is the last map of the last level, it returns with the flag gameCompleted true.
     */
    public Optional<Map> findCurrentMap(Game game, PlayerPhase lastPhaseCompleted) {
        if (lastPhaseCompleted == null) {
            return findFirstMapOfFirstLevel(game);
        }

        Optional<Phase> nextPhase = findNextPhase(lastPhaseCompleted);
        if (nextPhase.isPresent()) {
            return Optional.of(lastPhaseCompleted.getPhase().getMap());
        }

        Optional<Map> nextMapSameLevel = findNextMapSameLevel(lastPhaseCompleted);
        if (nextMapSameLevel.isPresent()) {
            return nextMapSameLevel;
        }

        Optional<Map> firstMapNextLevel = findFirstMapNextLevel(game, lastPhaseCompleted);
        if (firstMapNextLevel.isPresent()) {
            firstMapNextLevel.get().setLevelCompleted(true);
            return firstMapNextLevel;
        }

        Map map = lastPhaseCompleted.getPhase().getMap();
        map.setGameCompleted(true);
        return Optional.of(map);
    }

    private boolean isFirstMapOfFirstLevel(Map map) {
        return map.getLevel().getOrder() == 1 && map.getOrder() == 1;
    }

    private boolean isPreviousLevel(Map map, Phase lastPhaseDone) {
        return map.getLevel().getOrder() < lastPhaseDone.getMap().getLevel().getOrder();
    }

    private boolean isSameOrPreviousMapInSameLevel(Map map, Phase lastPhaseDone) {
        return map.getLevel().getOrder() == lastPhaseDone.getMap().getLevel().getOrder()
                && map.getOrder() <= lastPhaseDone.getMap().getOrder();
    }

    private boolean isLastPhaseOfLevel(Phase lastPhaseDone, Phase lastPhaseOfTheLevel) {
        return lastPhaseDone.getId() == lastPhaseOfTheLevel.getId();
    }

    private boolean isNextMapInSameLevel(Map map, Phase lastPhaseDone) {
        return lastPhaseDone.getMap().getLevel().getOrder() == map.getLevel().getOrder() && (lastPhaseDone.getMap().getOrder() + 1) == map.getOrder();
    }

    private boolean isFirstMapOfNextLevel(Map map, Phase lastPhaseDone) {
        return (lastPhaseDone.getMap().getLevel().getOrder() + 1) == map.getLevel().getOrder() && map.getOrder() == 1;
    }

    private Optional<Map> findFirstMapOfFirstLevel(Game game) {
        return this.findByGameAndLevel(game.getId(), 1);
    }

    private Optional<Phase> findNextPhase(PlayerPhase lastPhaseCompleted) {
        return phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(), lastPhaseCompleted.getPhase().getOrder() + 1);
    }

    private Optional<Map> findNextMapSameLevel(PlayerPhase lastPhaseCompleted) {
        return this.findNextMapSameLevel(lastPhaseCompleted.getPhase().getMap());
    }

    private Optional<Map> findFirstMapNextLevel(Game game, PlayerPhase lastPhaseCompleted) {
        return this.findByGameAndLevel(game.getId(), lastPhaseCompleted.getPhase().getMap().getLevel().getId() + 1);
    }
}
