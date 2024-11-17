package com.polifono.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.polifono.common.util.DateUtil;
import com.polifono.model.CurrentUser;
import com.polifono.model.dto.RankingDTO;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Phasestatus;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.model.form.teacher.ReportGeneralForm;
import com.polifono.repository.IPlayerPhaseRepository;
import com.polifono.service.game.GameService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerPhaseService {

    private final IPlayerPhaseRepository repository;
    private final GameService gameService;
    private final PhaseService phaseService;
    private final SecurityService securityService;

    public PlayerPhase save(PlayerPhase playerPhase) {
        return repository.save(playerPhase);
    }

    public List<PlayerPhase> findByPlayer(int playerId) {
        List<PlayerPhase> list = repository.findByPlayer(playerId);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;
    }

    public Optional<PlayerPhase> findByPlayerPhaseAndStatus(int playerId, int phaseId, int phasestatusId) {
        return repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId);
    }

    /**
     * Return the last phase that the player has completed in a specific game.
     */
    @NonNull
    public Optional<PlayerPhase> findLastPhaseCompleted(int playerId, int gameId) {
        List<PlayerPhase> playerPhases = repository.findLastPhaseCompleted(playerId, gameId);

        if (!playerPhases.isEmpty()) {
            return Optional.of(playerPhases.get(0));
        }

        return Optional.empty();
    }

    public List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, int playerId) {
        List<PlayerPhase> list = repository.findForReportGeneral(playerId, reportGeneralForm.getGame().getId(), reportGeneralForm.getPhaseBegin(),
                reportGeneralForm.getPhaseEnd());

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

    /**
     * Verify if the phase was already done by the player.
     */
    public boolean isPhaseAlreadyCompletedByPlayer(Phase phase, int playerId) {
        Optional<PlayerPhase> playerPhaseOpt = this.findByPlayerPhaseAndStatus(playerId, phase.getId(), 3);
        return playerPhaseOpt.isPresent();
    }

    /**
     * This method is used to register an attempt of doing the test.
     */
    public PlayerPhase setTestAttempt(Player player, Phase phase) {
        PlayerPhase playerPhase = this.findByPlayerPhaseAndStatus(player.getId(), phase.getId(), 2).orElse(null);

        // If this is not the first attempt.
        if (playerPhase != null) {
            playerPhase.setNumAttempts(playerPhase.getNumAttempts() + 1);
        } else {
            playerPhase = new PlayerPhase();

            playerPhase.setNumAttempts(1);
            playerPhase.setPhase(phase);
            Phasestatus phasestatus = new Phasestatus();
            phasestatus.setId(2);
            playerPhase.setPhasestatus(phasestatus);
            playerPhase.setPlayer(player);
        }

        return this.save(playerPhase);
    }

    /**
     * Based on a list of playerPhase, get the games only once.
     */
    public List<Game> filterPlayerPhasesListByGame(List<PlayerPhase> list) {
        List<Game> result = new ArrayList<>();
        Set<Integer> gameIds = new HashSet<>();

        for (PlayerPhase pf : list) {
            int currentGameId = pf.getPhase().getMap().getGame().getId();
            if (!gameIds.contains(currentGameId)) {
                gameIds.add(currentGameId);
                result.add(pf.getPhase().getMap().getGame());
            }
        }

        return result;
    }

    /**
     * This method is used to get the top 10 best students of the month.
     * The students that has achieved the highest scores.
     */
    // TODO - Add response to a cache.
    public List<RankingDTO> getRankingMonthly() {
        List<RankingDTO> ranking = new ArrayList<>();

        Object[][] objects;
        try {
            objects = repository.getRanking(DateUtil.getFirstDayOfTheCurrentMonth(), DateUtil.getLastDayOfTheCurrentMonth());
        } catch (ParseException e) {
            return ranking;
        }

        for (Object[] o : objects) {
            ranking.add(createRankingDTO(o));
        }

        return ranking;
    }

    public int getPermittedLevelForPlayer(int playerId, int gameId) {
        Optional<PlayerPhase> lastPlayerPhaseCompleted = findLastPhaseCompleted(playerId, gameId);
        if (lastPlayerPhaseCompleted.isEmpty()) {
            return 1;
        }

        Level lastLevel = lastPlayerPhaseCompleted.get().getPhase().getMap().getLevel();
        Optional<Phase> lastPhaseOfTheLevel = phaseService.findLastPhaseOfTheLevel(gameId, lastLevel.getId());
        return lastPhaseOfTheLevel.map(phase -> calculatePermittedLevel(lastPlayerPhaseCompleted.get(), phase, lastLevel)).orElse(1);
    }

    public PlayerPhase setupPlayerPhaseInProgress(Phase currentPhase, int grade) {
        PlayerPhase playerPhase = getPlayerPhaseInProgress(currentPhase);

        playerPhase.setGrade(grade);
        Phasestatus phasestatus = new Phasestatus();
        phasestatus.setId(3);
        playerPhase.setPhasestatus(phasestatus);
        playerPhase.setDtTest(new Date());
        playerPhase.setScore(gameService.calculateScore(playerPhase.getNumAttempts(), grade));

        return playerPhase;
    }

    public void registerTestAttempt(Phase phase) {
        Optional<CurrentUser> currentUser = securityService.getCurrentAuthenticatedUser();

        if (currentUser.isEmpty()) {
            return;
        }

        setTestAttempt(currentUser.get().getUser(), phase);
    }

    private PlayerPhase getPlayerPhaseInProgress(Phase currentPhase) {
        return findByPlayerPhaseAndStatus(securityService.getUserId(), currentPhase.getId(), 2).orElseGet(() -> createNewPlayerPhase(currentPhase));
    }

    private PlayerPhase createNewPlayerPhase(Phase currentPhase) {
        PlayerPhase playerPhase = new PlayerPhase();
        Player player = securityService.getUser();
        playerPhase.setPlayer(player);
        playerPhase.setNumAttempts(1);
        playerPhase.setPhase(currentPhase);
        return playerPhase;
    }

    private RankingDTO createRankingDTO(Object[] o) {
        Player p = new Player();
        p.setId((int) o[0]);
        p.setName((String) o[1]);
        p.setLastName((String) o[2]);

        return new RankingDTO(p, ((Number) o[3]).intValue());
    }

    private int calculatePermittedLevel(PlayerPhase lastPlayerPhaseCompleted, Phase lastPhaseOfTheLevel, Level lastLevel) {
        if (lastPlayerPhaseCompleted.getPhase().getId() != lastPhaseOfTheLevel.getId()) {
            return lastLevel.getOrder();
        } else {
            return lastLevel.getOrder() + 1;
        }
    }
}
