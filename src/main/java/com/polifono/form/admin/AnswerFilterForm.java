package com.polifono.form.admin;

import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Question;

public class AnswerFilterForm {

	private Game game;
	private Level level;
	private Map map;
	private Phase phase;
	private Question question;
	
	public AnswerFilterForm() {
		this.game = new Game();
		this.level = new Level();
		this.map = new Map();
		this.phase = new Phase();
		this.question = new Question();
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

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}