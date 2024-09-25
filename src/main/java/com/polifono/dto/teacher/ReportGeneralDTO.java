package com.polifono.dto.teacher;

import java.util.List;

import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;

import lombok.Data;

@Data
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
