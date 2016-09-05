package com.polifono.dto.teacher;

import java.util.List;

import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;

public class ReportGeneralDTO {

	private int phaseBegin;
	private int phaseEnd;
	private Player player;
	private List<PlayerPhase> playerPhase;
	
	public ReportGeneralDTO(int phaseBegin, int phaseEnd, Player player, List<PlayerPhase> playerPhase) {
		this.phaseBegin = phaseBegin;
		this.phaseEnd = phaseEnd;
		this.player = player;
		this.playerPhase = playerPhase;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<PlayerPhase> getPlayerPhase() {
		return playerPhase;
	}

	public void setPlayerPhase(List<PlayerPhase> playerPhase) {
		this.playerPhase = playerPhase;
	}
	
	public int getPhaseBegin() {
		return phaseBegin;
	}

	public void setPhaseBegin(int phaseBegin) {
		this.phaseBegin = phaseBegin;
	}

	public int getPhaseEnd() {
		return phaseEnd;
	}

	public void setPhaseEnd(int phaseEnd) {
		this.phaseEnd = phaseEnd;
	}

	public int getScore() {
		int score = 0;
		
		for (PlayerPhase playerPhase : this.playerPhase) {
			score += playerPhase.getScore();
		}
		
		return score;
	}
	
	public double getAverage() {
		double average = ( (double) getScore() / ((getPhaseEnd() - getPhaseBegin()) + 1));
		
		average = average * 100;
		average = Math.round(average);
		average = average /100;
		
		return average;
	}
}