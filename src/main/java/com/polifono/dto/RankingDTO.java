package com.polifono.dto;

import com.polifono.domain.Player;

public class RankingDTO {

	private Player player;
	private int score;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}