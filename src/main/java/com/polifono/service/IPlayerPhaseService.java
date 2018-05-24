package com.polifono.service;

import java.util.List;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;

public interface IPlayerPhaseService {
	
	public PlayerPhase save(PlayerPhase o);
	
	//public Boolean delete(Integer id);
	
	//public PlayerPhase findOne(int id);
		
	//public List<PlayerPhase> findAll();
	
	
	public List<PlayerPhase> findByPlayer(int playerId);
	
	public PlayerPhase findByPlayerPhaseAndStatus(int playerId, int phaseId, int phasestatusId);
	
	public PlayerPhase findLastPhaseCompleted(int playerId, int gameId);
	
	
	public List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, int playerId);

	public boolean isPhaseAlreadyCompletedByPlayer(Phase phase, Player user);

	public PlayerPhase setTestAttempt(Player user, Phase phase);
	
	public List<Game> filterPlayerPhasesListByGame(List<PlayerPhase> list);

}