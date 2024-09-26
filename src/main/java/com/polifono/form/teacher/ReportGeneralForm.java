package com.polifono.form.teacher;

import com.polifono.domain.Game;

import lombok.Data;

@Data
public class ReportGeneralForm {

	private Game game;
	private com.polifono.domain.Class clazz;
	private int phaseBegin;
	private int phaseEnd;

	public ReportGeneralForm() {
		this.game = new Game();
		this.clazz = new com.polifono.domain.Class();
	}
}
