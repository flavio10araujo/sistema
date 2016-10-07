package com.polifono.service;

import java.util.List;

import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;

public interface IPlayerPhaseService {

	public PlayerPhase save(PlayerPhase playerPhase);
	
	public PlayerPhase findLastPhaseCompleted(int playerId, int gameId);
	
	public PlayerPhase findByPlayerPhaseAndStatus(int playerId, int phaseId, int phasestatusId);
	
	public List<PlayerPhase> findPlayerPhasesByPlayer(int playerId);
	
	public List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, int playerId);

	public boolean isPhaseAlreadyCompletedByPlayer(Phase phase, Player user);

	public PlayerPhase setTestAttempt(Player user, Phase phase);

}