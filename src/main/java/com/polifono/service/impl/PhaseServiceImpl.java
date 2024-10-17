package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.PlayerPhase;
import com.polifono.repository.IPhaseRepository;
import com.polifono.service.IPhaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PhaseServiceImpl implements IPhaseService {

    private final IPhaseRepository repository;

    @Override
    public Phase save(Phase phase) {
        return repository.save(phase);
    }

    @Override
    public boolean delete(Integer id) {
        Optional<Phase> temp = repository.findById(id);

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
    public Optional<Phase> findById(int phaseId) {
        return repository.findById(phaseId);
    }

    @Override
    public List<Phase> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Phase> findByGame(int gameId) {
        return repository.findByGame(gameId);
    }

    @Override
    public List<Phase> findByGameAndLevel(int gameId, int levelId) {
        return repository.findByGameAndLevel(gameId, levelId);
    }

    @Override
    public List<Phase> findByMap(int mapId) {
        return repository.findByMap(mapId);
    }

    @Override
    public Optional<Phase> findByMapAndOrder(int mapId, int phaseOrder) {
        return repository.findByMapAndOrder(mapId, phaseOrder);
    }

    /**
     * phaseOrder = it's the order of the phase that will be returned.
     */
    @Override
    public Optional<Phase> findNextPhaseInThisMap(int mapId, int phaseOrder) {
        return repository.findNextPhaseInThisMap(mapId, phaseOrder);
    }

    @Override
    public Optional<Phase> findLastPhaseDoneByPlayerAndGame(int playerId, int gameId) {
        List<Phase> list = repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId);

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public Optional<Phase> findLastPhaseOfTheLevel(int gameId, int levelId) {
        List<Phase> list = repository.findLastPhaseOfTheLevel(gameId, levelId);

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public List<Phase> findGamesForProfile(int playerId) {
        List<Phase> list = repository.findGamesForProfile(playerId);

        if (list.isEmpty()) {
            return null;
        }

        List<Phase> ret = new ArrayList<>();

        int gameId = 0;

        for (Phase phase : list) {
            if (gameId != phase.getMap().getGame().getId()) {
                gameId = phase.getMap().getGame().getId();
                ret.add(phase);
            }
        }

        return ret;
    }

    /**
     * Get the phases of the map.
     * Check which phases are opened.
     * The phases opened are: all the phase that the player has already done + next phase.
     */
    @Override
    public List<Phase> findPhasesCheckedByMap(Map map, PlayerPhase lastPhaseCompleted) {
        List<Phase> phases = this.findByMap(map.getId());

        // If there are no phases in the map.
        if (phases == null || phases.isEmpty()) {
            return null;
        }

        // If the player has never completed any phase of this game.
        if (lastPhaseCompleted == null) {
            // Open the first phase of the map.
            phases.get(0).setOpened(true);
        }
        // If the player has already completed at least one phase of this game.
        else {
            // Open all phases until the next phase.
            for (Phase phase : phases) {
                if (phase.getOrder() <= (lastPhaseCompleted.getPhase().getOrder() + 1)) {
                    phase.setOpened(true);
                }
            }
        }

        return phases;
    }

    /**
     * Verify if the player has permission to access a specific phase.
     * Return true if the player has the permission.
     */
    @Override
    public boolean canPlayerAccessPhase(Phase phase, int playerId) {
        // The first phase is always allowed.
        if (phase.getOrder() == 1) {
            return true;
        }

        // Get the last phase that the player has done in a specific game.
        Optional<Phase> lastPhaseDone = findLastPhaseDoneByPlayerAndGame(playerId, phase.getMap().getGame().getId());

        // If the player is trying to access a phase, but he had never finished a phase of this game.
        if (lastPhaseDone.isEmpty()) {
            return false;
        }

        // If the player is trying to access a phase that he had already done OR the next phase in the right sequence.
        return lastPhaseDone.get().getOrder() >= (phase.getOrder() - 1);
    }

    @Override
    public List<Phase> findPhasesBySearchAndUser(String q, int playerId) {
        return repository.findPhasesBySearchAndUser("%" + q + "%", playerId, 3);
    }
}
