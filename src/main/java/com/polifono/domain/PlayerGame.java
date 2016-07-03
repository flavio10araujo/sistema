package com.polifono.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t016_player_game")
public class PlayerGame {

	@Id
	@Column(name = "c016_id")
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "c001_id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "c002_id")
	private Game game;
	
	@Column(name = "c016_credit")
	private int credit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
}