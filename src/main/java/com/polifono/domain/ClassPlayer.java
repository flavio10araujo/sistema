package com.polifono.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t015_class_player")
public class ClassPlayer {
    @Id
    @Column(name = "c015_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "c014_id")
    private Class clazz;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @Column(name = "c015_dt_inc")
    private Date dtInc;

    @Column(name = "c015_active")
    private boolean active;

    @Column(name = "c015_dt_exc")
    private Date dtExc;

    @Column(name = "c015_status")
    private int status;
}
