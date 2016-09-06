package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;

public interface IPlayerPhaseService {

	public PlayerPhase save(PlayerPhase playerPhase);
	
	public PlayerPhase findLastPhaseCompleted(Player player, Game game);
	
	public PlayerPhase findPlayerPhaseByPlayerPhaseAndStatus(Player player, Phase phase, int phasestatusId);
	
	public List<PlayerPhase> findPlayerPhaseByPlayer(Player player);
	
	public List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, Player player);

}