package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.repository.IPlayerPhaseRepository;
import com.polifono.service.IPlayerPhaseService;

@Service
public class PlayerPhaseServiceImpl implements IPlayerPhaseService {

	@Autowired
	private IPlayerPhaseRepository repository;
	
	public final PlayerPhase save(PlayerPhase playerPhase) {
		return repository.save(playerPhase);
	}
	
	/**
	 * Return the last phase that the player has completed.
	 * 
	 * @param player
	 * @param game
	 * @return
	 */
	public final PlayerPhase findLastPhaseCompleted(int playerId, int gameId) {
		List<PlayerPhase> playerPhases = repository.findLastPhaseCompleted(playerId, gameId);
		
		if (playerPhases.size() > 0) {
			return playerPhases.get(0);
		}
		
		return null;
	}
	
	public final PlayerPhase findByPlayerPhaseAndStatus(int playerId, int phaseId, int phasestatusId) {
		return repository.findByPlayerPhaseAndStatus(playerId, phaseId, phasestatusId);
	}
	
	public final List<PlayerPhase> findPlayerPhasesByPlayer(int playerId) {
		List<PlayerPhase> list = repository.findPlayerPhasesByPlayer(playerId); 
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return list;
	}
	
	/**
	 * 
	 * 
	 * @param playerId
	 * @return
	 */
	public final List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, int playerId) {
		List<PlayerPhase> list = repository.findForReportGeneral(playerId, reportGeneralForm.getGame().getId(), reportGeneralForm.getPhaseBegin(), reportGeneralForm.getPhaseEnd()); 
		
		if (list == null) {
			return new ArrayList<PlayerPhase>();
		}
		
		return list;
	}
}