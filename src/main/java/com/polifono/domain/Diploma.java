package com.polifono.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t017_diploma")
public class Diploma {

	@Id
	@Column(name = "c017_id")
	@GeneratedValue
	private int id;
	
	@Column(name = "c017_dt")
	private Date dt;
	
	@ManyToOne
	@JoinColumn(name = "c001_id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "c002_id")
	private Game game;
	
	@ManyToOne
	@JoinColumn(name = "c003_id")
	private Level level;
	
	@Column(name = "c017_code")
	private String code;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
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

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}