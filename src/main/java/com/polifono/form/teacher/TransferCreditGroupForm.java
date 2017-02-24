package com.polifono.form.teacher;

import com.polifono.domain.Game;

public class TransferCreditGroupForm {

	private Game game;
	private com.polifono.domain.Class clazz;
	private int credit;
	
	public TransferCreditGroupForm() {
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

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
}