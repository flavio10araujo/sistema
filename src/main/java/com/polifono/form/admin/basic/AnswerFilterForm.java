package com.polifono.form.admin.basic;

import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Question;

import lombok.Data;

@Data
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
}
