package com.polifono.form.admin.basic;

import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;

import lombok.Data;

@Data
public class PhaseFilterForm {

	private Game game;
	private Level level;
	private Map map;

	public PhaseFilterForm() {
		this.game = new Game();
		this.level = new Level();
		this.map = new Map();
	}
}
