package com.polifono.form.teacher;

import com.polifono.domain.Game;

import lombok.Data;

@Data
public class TransferCreditGroupForm {

	private Game game;
	private com.polifono.domain.Class clazz;
	private int credit;

	public TransferCreditGroupForm() {
		this.game = new Game();
		this.clazz = new com.polifono.domain.Class();
	}
}
