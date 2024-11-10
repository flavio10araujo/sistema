package com.polifono.model.form.admin.basic;

import com.polifono.model.entity.Game;
import com.polifono.model.entity.Level;
import com.polifono.model.entity.Map;
import com.polifono.model.entity.Phase;

import lombok.Data;

@Data
public class ContentFilterForm {

    private Game game;
    private Level level;
    private Map map;
    private Phase phase;

    public ContentFilterForm() {
        this.game = new Game();
        this.level = new Level();
        this.map = new Map();
        this.phase = new Phase();
    }
}
