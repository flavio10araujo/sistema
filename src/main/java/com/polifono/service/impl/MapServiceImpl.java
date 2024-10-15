package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.repository.IMapRepository;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MapServiceImpl implements IMapService {

    private final IMapRepository repository;
    private final IPhaseService phaseService;

    @Override
    public Map save(Map map) {
        return repository.save(map);
    }

    @Override
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

    @Override
    public Optional<Map> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Map> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Map> findMapsByGame(int gameId) {
        return repository.findMapsByGame(gameId);
    }

    @Override
    public List<Map> findMapsByGameAndLevel(int gameId, int levelId) {
        return repository.findMapsByGameAndLevel(gameId, levelId);
    }

    @Override
    public Optional<Map> findByGameAndLevel(int gameId, int levelId) {
        List<Map> maps = repository.findMapsByGameAndLevel(gameId, levelId);

        if (!maps.isEmpty()) {
            return Optional.of(maps.get(0));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Map> findByGameLevelAndOrder(int gameId, int levelId, int mapOrder) {
        List<Map> maps = repository.findMapsByGameLevelAndOrder(gameId, levelId, mapOrder);

        if (!maps.isEmpty()) {
            return Optional.of(maps.get(0));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Map> findNextMapSameLevel(Map mapCurrent) {
        return repository.findNextMapSameLevel(mapCurrent.getGame().getId(), mapCurrent.getLevel().getId(), mapCurrent.getOrder());
    }

    /**
     * Verify if the player has permission to access a specific map.
     * Return true if the player has the permission.
     */
    @Override
    public boolean playerCanAccessThisMap(Map map, Player player) {

        // The first map of the first level is always permitted.
        if (map.getLevel().getOrder() == 1 && map.getOrder() == 1) {
            return true;
        }

        // Get the last phase that the player has done in a specific game.
        Optional<Phase> lastPhaseDone = phaseService.findLastPhaseDoneByPlayerAndGame(player.getId(), map.getGame().getId());

        // If the player is trying to access a map different of the first map of the first level and he never had finished a phase of this game.
        if (lastPhaseDone.isEmpty()) {
            return false;
        }

        // If the player is trying to access a map in a previous level than the lastPhaseDone's level.
        if (map.getLevel().getOrder() < lastPhaseDone.get().getMap().getLevel().getOrder()) {
            return true;
        }

        // If the player is trying to access a previous map (or the same map) at the same level of the lastPhaseDone.
        if (
                map.getLevel().getOrder() == lastPhaseDone.get().getMap().getLevel().getOrder()
                        && map.getOrder() <= lastPhaseDone.get().getMap().getOrder()
        ) {
            return true;
        }

        // If you are here, it's because the player is trying to access a next map at the same level OR a map in one of the next levels.

        // Get the last phase of the map of the lastPhaseDone.
        Optional<Phase> lastPhaseOfTheLevel = phaseService.findLastPhaseOfTheLevel(lastPhaseDone.get().getMap().getGame().getId(),
                lastPhaseDone.get().getMap().getLevel().getId());

        if (lastPhaseOfTheLevel.isEmpty()) {
            return false;
        }

        // If the lastPhaseDone is not the lastPhaseOfTheLevel. Then the player can't access the next map or the next level.
        if (lastPhaseDone.get().getId() != lastPhaseOfTheLevel.get().getId()) {
            return false;
        }

        // If the player is trying to access a map in the same level.
        if (lastPhaseDone.get().getMap().getLevel().getOrder() == map.getLevel().getOrder()) {
            // If it is the next map.
            if ((lastPhaseDone.get().getMap().getOrder() + 1) == map.getOrder()) {
                return true;
            } else {
                return false;
            }
        }

        // If the player is trying to access a map in the next level.
        if ((lastPhaseDone.get().getMap().getLevel().getOrder() + 1) == map.getLevel().getOrder()) {
            // If it is the first map.
            return map.getOrder() == 1;
        }

        return false;
    }

    /**
     * Get the current map based on the last phase completed by the player in a specific game.
     * If the map is the last map of the level, it returns with the flag levelCompleted true.
     * If the map is the last map of the last level, it returns with the flag gameCompleted true.
     */
    @Override
    public Optional<Map> findCurrentMap(Game game, PlayerPhase lastPhaseCompleted) {

        // If the player has never completed any phase of this game.
        if (lastPhaseCompleted == null) {
            // Find the first map of the first level of this game.
            return this.findByGameAndLevel(game.getId(), 1);
        }
        // If the player has already completed at least one phase of this game.
        else {
            // Verifying is the next phase is in the same map than lastPhaseCompleted.
            Optional<Phase> nextPhase = phaseService.findNextPhaseInThisMap(lastPhaseCompleted.getPhase().getMap().getId(),
                    lastPhaseCompleted.getPhase().getOrder() + 1);

            // If the next phase is in the same map that the last phase completed by the player in this game.
            if (nextPhase.isPresent()) {
                return Optional.of(lastPhaseCompleted.getPhase().getMap());
            }
            // If the next phase is in the next map or in the next level.
            else {
                // Checking if the next map is in the same level.
                Optional<Map> nextMapSameLevel = this.findNextMapSameLevel(lastPhaseCompleted.getPhase().getMap());

                // If the next map is in the same level.
                if (nextMapSameLevel.isPresent()) {
                    return nextMapSameLevel;
                }
                // If the next map is not in the same level.
                else {
                    // Find the first map of the next level.
                    Optional<Map> firstMapNextLevel = this.findByGameAndLevel(game.getId(), lastPhaseCompleted.getPhase().getMap().getLevel().getId() + 1);

                    // If it has found the first map of the next level.
                    if (firstMapNextLevel.isPresent()) {
                        firstMapNextLevel.get().setLevelCompleted(true);
                        return firstMapNextLevel;
                    }
                    // It doesn't exist a next map, because the player has already finished the last phase of the last level.
                    else {
                        // Draw the last map of the last level.
                        Map map = lastPhaseCompleted.getPhase().getMap();
                        map.setGameCompleted(true);
                        return Optional.of(map);
                    }
                }
            }
        }
    }
}
