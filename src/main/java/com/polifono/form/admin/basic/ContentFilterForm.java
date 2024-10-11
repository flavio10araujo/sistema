package com.polifono.form.admin.basic;

import com.polifono.domain.Game;
import com.polifono.domain.Level;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;

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
