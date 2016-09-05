package com.polifono.form.admin;

import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;

public class QuestionFilterForm {

	private Game game;
	private Level level;
	private Map map;
	private Phase phase;
	
	public QuestionFilterForm() {
		this.game = new Game();
		this.level = new Level();
		this.map = new Map();
		this.phase = new Phase();
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

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}
}