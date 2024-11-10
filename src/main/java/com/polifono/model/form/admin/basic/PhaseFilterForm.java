package com.polifono.model.form.admin.basic;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Map;

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
