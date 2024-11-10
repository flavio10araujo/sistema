package com.polifono.model.form.teacher;

import com.polifono.model.entity.Class;
import com.polifono.model.entity.Game;

import lombok.Data;

@Data
public class TransferCreditGroupForm {

    private Game game;
    private Class clazz;
    private int credit;

    public TransferCreditGroupForm() {
        this.game = new Game();
        this.clazz = new Class();
    }
}
