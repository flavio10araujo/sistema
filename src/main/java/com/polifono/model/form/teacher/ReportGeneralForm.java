package com.polifono.model.form.teacher;

import com.polifono.model.entity.Class;
import com.polifono.model.entity.Game;

import lombok.Data;

@Data
public class ReportGeneralForm {

    private Game game;
    private Class clazz;
    private int phaseBegin;
    private int phaseEnd;

    public ReportGeneralForm() {
        this.game = new Game();
        this.clazz = new Class();
    }
}
