package com.polifono.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Phasestatus;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.dto.RankingDTO;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.repository.IPlayerPhaseRepository;
import com.polifono.service.IPlayerPhaseService;
import com.polifono.util.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerPhaseServiceImpl implements IPlayerPhaseService {

    private final IPlayerPhaseRepository repository;

    public final PlayerPhase save(PlayerPhase playerPhase) {
        return repository.save(playerPhase);
    }

    /**
     * Return the last phase that the player has completed.
     */
    public final PlayerPhase findLastPhaseCompleted(int playerId, int gameId) {
        List<PlayerPhase> playerPhases = repository.findLastPhaseCompleted(playerId, gameId);

        if (!playerPhases.isEmpty()) {
            return playerPhases.get(0);
        }

        return null;
    }

    public final PlayerPhase findByPlayerPhaseAndStatus(int playerId, int phaseId, int phasestatusId) {
        return repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId);
    }

    public final List<PlayerPhase> findByPlayer(int playerId) {
        List<PlayerPhase> list = repository.findByPlayer(playerId);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list;
    }

    /**
     * Based on a list of playerPhase, get the games only once.
     */
    public final List<Game> filterPlayerPhasesListByGame(List<PlayerPhase> list) {
        List<Game> ret = new ArrayList<>();

        int gameId = 0;

        for (PlayerPhase pf : list) {
            if (gameId != pf.getPhase().getMap().getGame().getId()) {
                gameId = pf.getPhase().getMap().getGame().getId();
                ret.add(pf.getPhase().getMap().getGame());
            }
        }

        return ret;
    }

    public final List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, int playerId) {
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
    public boolean isPhaseAlreadyCompletedByPlayer(Phase phase, Player player) {

        PlayerPhase playerPhase = this.findByPlayerPhaseAndStatus(player.getId(), phase.getId(), 3);

        // The phase is already completed by this player.
        return playerPhase != null;
    }

    /**
     * This method is used to register an attempt of doing the test.
     */
    public PlayerPhase setTestAttempt(Player player, Phase phase) {
        PlayerPhase playerPhase = this.findByPlayerPhaseAndStatus(player.getId(), phase.getId(), 2);

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
     * This method is used to get the top 10 best students of the month.
     * The students that has achieved the highest scores.
     */
    public List<RankingDTO> getRankingMonthly() {
        List<RankingDTO> ranking = new ArrayList<>();

        Object[][] objects;
        try {
            objects = repository.getRanking(DateUtil.getFirstDayOfTheCurrentMonth(), DateUtil.getLastDayOfTheCurrentMonth());
        } catch (ParseException e) {
            log.error("getRankingMonthly : ", e);
            return ranking;
        }

        for (Object[] o : objects) {
            Player p = new Player();
            p.setId((int) o[0]);
            p.setName((String) o[1]);
            p.setLastName((String) o[2]);

            RankingDTO r = new RankingDTO(p, ((Number) o[3]).intValue());
            ranking.add(r);
        }

        return ranking;
    }
}
