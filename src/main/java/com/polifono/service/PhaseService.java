package com.polifono.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.repository.IPhaseRepository;
import com.polifono.service.player.PlayerCreditService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PhaseService {

    private final IPhaseRepository repository;
    private final SecurityService securityService;
    private final PlayerCreditService playerCreditService;

    public Phase save(Phase phase) {
        return repository.save(phase);
    }

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

    public Optional<Phase> findById(int phaseId) {
        return repository.findById(phaseId);
    }

    public List<Phase> findAll() {
        return repository.findAll();
    }

    public List<Phase> findByGame(int gameId) {
        return repository.findByGame(gameId);
    }

    public List<Phase> findByGameAndLevel(int gameId, int levelId) {
        return repository.findByGameAndLevel(gameId, levelId);
    }

    public List<Phase> findByMap(int mapId) {
        return repository.findByMap(mapId);
    }

    public Optional<Phase> findByMapAndOrder(int mapId, int phaseOrder) {
        return repository.findByMapAndOrder(mapId, phaseOrder);
    }

    /**
     * phaseOrder = it's the order of the phase that will be returned.
     */
    public Optional<Phase> findNextPhaseInThisMap(int mapId, int phaseOrder) {
        return repository.findNextPhaseInThisMap(mapId, phaseOrder);
    }

    public Optional<Phase> findLastPhaseDoneByPlayerAndGame(int playerId, int gameId) {
        List<Phase> list = repository.findLastPhaseDoneByPlayerAndGame(playerId, gameId);

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public Optional<Phase> findLastPhaseOfTheLevel(int gameId, int levelId) {
        List<Phase> list = repository.findLastPhaseOfTheLevel(gameId, levelId);

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public List<Phase> findGamesForProfile(int playerId) {
        List<Phase> list = repository.findGamesForProfile(playerId);

        if (list.isEmpty()) {
            return null;
        }

        return list.stream()
                .filter(phase -> phase.getMap().getGame().getId() != 0)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get the phases of the map.
     * Check which phases are opened.
     * The phases opened are: all the phase that the player has already done + next phase.
     */
    public List<Phase> findPhasesCheckedByMap(Map map, PlayerPhase lastPhaseCompleted) {
        List<Phase> phases = this.findByMap(map.getId());

        // If there are no phases in the map.
        if (phases == null || phases.isEmpty()) {
            return null;
        }

        // If the player has never completed any phase of this game.
        if (lastPhaseCompleted == null) {
            // Open the first phase of the map.
            openFirstPhaseIfNoCompletion(phases);
        }
        // If the player has already completed at least one phase of this game.
        else {
            // Open all phases until the next phase.
            openPhasesUntilNext(phases, lastPhaseCompleted);
        }

        return phases;
    }

    /**
     * Verify if the player has permission to access a specific phase.
     * Return true if the player has the permission.
     */
    public boolean canPlayerAccessPhase(Phase phase, int playerId) {
        if (isFirstPhase(phase)) {
            return true;
        }

        Optional<Phase> lastPhaseDone = findLastPhaseDoneByPlayerAndGame(playerId, phase.getMap().getGame().getId());
        return lastPhaseDone.filter(value -> hasPlayerCompletedOrNextPhase(value, phase)).isPresent();
    }

    /**
     * Looking for the phases that have the q in its content and the user has already studied.
     */
    public List<Phase> findPhasesBySearchAndUser(String q, int playerId) {
        return repository.findPhasesBySearchAndUser("%" + q + "%", playerId, 3);
    }

    public boolean hasPlayerPassedPhase(int grade) {
        return (grade >= 70);
    }

    public boolean shouldDisplayBuyCreditsPage(Game game, Phase phase) {
        // If the player doesn't have credits anymore.
        // And the player is not trying to access the first phase (the first phase is always free).
        if (isTryingToAccessNotFreePhaseWithoutCredits(phase)) {
            // Get the last phase that the player has finished in a specific game.
            Optional<Phase> lastPhaseDoneOpt = findLastPhaseDoneByPlayerAndGame(securityService.getUserId(), game.getId());

            return lastPhaseDoneOpt.isEmpty() || lastPhaseDoneOpt.get().getOrder() < phase.getOrder();
        }

        return false;
    }

    private boolean isTryingToAccessNotFreePhaseWithoutCredits(Phase phase) {
        return !playerCreditService.playerHasCredits(securityService.getUserId(), phase) && phase.getOrder() > 1;
    }

    private void openFirstPhaseIfNoCompletion(List<Phase> phases) {
        if (phases != null && !phases.isEmpty()) {
            phases.get(0).setOpened(true);
        }
    }

    private void openPhasesUntilNext(List<Phase> phases, PlayerPhase lastPhaseCompleted) {
        if (phases != null && !phases.isEmpty()) {
            for (Phase phase : phases) {
                if (phase.getOrder() <= (lastPhaseCompleted.getPhase().getOrder() + 1)) {
                    phase.setOpened(true);
                }
            }
        }
    }

    private boolean isFirstPhase(Phase phase) {
        return phase.getOrder() == 1;
    }

    private boolean hasPlayerCompletedOrNextPhase(Phase lastPhaseDone, Phase phase) {
        return lastPhaseDone.getOrder() >= (phase.getOrder() - 1);
    }
}
