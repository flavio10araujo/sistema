package com.polifono.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Game;
import com.polifono.domain.Phase;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;
import com.polifono.form.teacher.ReportGeneralForm;
import com.polifono.repository.PlayerPhaseRepository;

@Service
public class PlayerPhaseService {

	@Autowired
	private PlayerPhaseRepository playerPhaseRepository;
	
	public final PlayerPhase save(PlayerPhase playerPhase) {
		return playerPhaseRepository.save(playerPhase);
	}
	
	/**
	 * Return the last phase that the player has completed.
	 * 
	 * @param player
	 * @param game
	 * @return
	 */
	public final PlayerPhase findLastPhaseCompleted(Player player, Game game) {
		List<PlayerPhase> playerPhases = playerPhaseRepository.findLastPhaseCompleted(player.getId(), game.getId());
		
		if (playerPhases.size() > 0) {
			return playerPhases.get(0);
		}
		
		return null;
	}
	
	public final PlayerPhase findPlayerPhaseByPlayerPhaseAndStatus(Player player, Phase phase, int phasestatusId) {
		return playerPhaseRepository.findPlayerPhaseByPlayerPhaseAndStatus(player.getId(), phase.getId(), phasestatusId);
	}
	
	public final List<PlayerPhase> findPlayerPhaseByPlayer(Player player) {
		List<PlayerPhase> list = playerPhaseRepository.findPlayerPhaseByPlayer(player.getId()); 
		
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
	public final List<PlayerPhase> findForReportGeneral(ReportGeneralForm reportGeneralForm, Player player) {
		List<PlayerPhase> list = playerPhaseRepository.findForReportGeneral(player.getId(), reportGeneralForm.getGame().getId(), reportGeneralForm.getPhaseBegin(), reportGeneralForm.getPhaseEnd()); 
		
		if (list == null || list.size() == 0) {
			return new ArrayList<PlayerPhase>();
		}
		
		return list;
	}
}