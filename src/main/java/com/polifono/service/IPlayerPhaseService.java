package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.model.dto.RankingDTO;
import com.polifono.model.entity.Game;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;
import com.polifono.model.entity.PlayerPhase;
import com.polifono.model.form.teacher.ReportGeneralForm;

public interface IPlayerPhaseService {

    PlayerPhase save(PlayerPhase o);

    List<PlayerPhase> findByPlayer(int playerId);

    Optional<PlayerPhase> findByPlayerPhaseAndStatus(int playerId, int phaseId, int phasestatusId);

    Optional<PlayerPhase> findLastPhaseCompleted(int playerId, int gameId);

    List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, int playerId);

    boolean isPhaseAlreadyCompletedByPlayer(Phase phase, int playerId);

    PlayerPhase setTestAttempt(Player user, Phase phase);

    List<Game> filterPlayerPhasesListByGame(List<PlayerPhase> list);

    List<RankingDTO> getRankingMonthly();

    int getPermittedLevelForPlayer(int playerId, int gameId);

    PlayerPhase setupPlayerPhaseInProgress(Phase currentPhase, int grade);

    void registerTestAttempt(Phase phase);
}
