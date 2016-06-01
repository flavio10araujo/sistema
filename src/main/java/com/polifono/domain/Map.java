package com.polifono.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t004_map")
public class Map {

	@Id
	@Column(name = "c004_id")
	@GeneratedValue
	private int id;
	
	@Column(name = "c004_name")
	private String name;
	
	@Column(name = "c004_order")
	private int order;
	
	@ManyToOne
	@JoinColumn(name = "c002_id")
	private Game game;
	
	@ManyToOne
	@JoinColumn(name = "c003_id")
	private Level level;
	
	@Transient
	boolean levelCompleted = false;
	
	@Transient
	boolean gameCompleted = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
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

	public boolean isLevelCompleted() {
		return levelCompleted;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.levelCompleted = levelCompleted;
	}

	public boolean isGameCompleted() {
		return gameCompleted;
	}

	public void setGameCompleted(boolean gameCompleted) {
		this.gameCompleted = gameCompleted;
	}
}