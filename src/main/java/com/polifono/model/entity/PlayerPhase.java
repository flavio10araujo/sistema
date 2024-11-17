package com.polifono.model.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "t007_player_phase")
public class PlayerPhase {
    @Id
    @Column(name = "c007_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "c005_id")
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "c006_id")
    private Phasestatus phasestatus;

    @Column(name = "c007_grade")
    private double grade;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "c007_dt_test")
    private Date dtTest;

    @Column(name = "c007_num_attempts")
    private int numAttempts;

    @Column(name = "c007_score")
    private int score;
}
