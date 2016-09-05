package com.polifono.form.admin;

import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;

public class PhaseFilterForm {

	private Game game;
	private Level level;
	private Map map;
	
	public PhaseFilterForm() {
		this.game = new Game();
		this.level = new Level();
		this.map = new Map();
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
	
	public Map getMap() {
		return map;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
}