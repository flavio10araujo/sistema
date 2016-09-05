package com.polifono.form.teacher;

import com.polifono.domain.Game;

public class ReportGeneralForm {

	private Game game;
	private com.polifono.domain.Class clazz;
	private int phaseBegin;
	private int phaseEnd;
	
	public ReportGeneralForm() {
		this.game = new Game();
		this.clazz = new com.polifono.domain.Class();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public com.polifono.domain.Class getClazz() {
		return clazz;
	}

	public void setClazz(com.polifono.domain.Class clazz) {
		this.clazz = clazz;
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
}