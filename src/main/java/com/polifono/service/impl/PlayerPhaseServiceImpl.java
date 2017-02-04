package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Phasestatus;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.repository.IPlayerPhaseRepository;
import com.polifono.service.IPlayerPhaseService;

@Service
public class PlayerPhaseServiceImpl implements IPlayerPhaseService {

	private IPlayerPhaseRepository repository;
	
	@Autowired
	public PlayerPhaseServiceImpl(IPlayerPhaseRepository repository) {
		this.repository = repository;
	}
	
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
	 * Based on a list of playerPhase, get the games only once.
	 * 
	 * @param list
	 * @return
	 */
	public final List<Game> filterPlayerPhasesListByGame(List<PlayerPhase> list) {
		List<Game> ret = new ArrayList<Game>();
		
		int gameId = 0;
		
		for (PlayerPhase pf : list) {
			if (gameId != pf.getPhase().getMap().getGame().getId()) {
				gameId = pf.getPhase().getMap().getGame().getId();
				ret.add(pf.getPhase().getMap().getGame());
			}
		}
		
		return ret;
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
	
	/**
	 * Verify if the phase was already done by the player.
	 * 
	 * @param phase
	 * @return
	 */
	public boolean isPhaseAlreadyCompletedByPlayer(Phase phase, Player player) {

		PlayerPhase playerPhase = this.findByPlayerPhaseAndStatus(player.getId(), phase.getId(), 3);
		
		// The phase is already completed by this player.
		if (playerPhase != null) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method is used to register an attempt of doing the test.
	 * 
	 * @param phase
	 */
	public PlayerPhase setTestAttempt(Player player, Phase phase) {
		PlayerPhase playerPhase = this.findByPlayerPhaseAndStatus(player.getId(), phase.getId(), 2);
		
		// If this is not the first attempt.
		if (playerPhase != null) {
			playerPhase.setNumAttempts(playerPhase.getNumAttempts() + 1);
		}
		else {
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
}