package com.polifono.model.entity;

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
@Table(name = "t016_player_game")
public class PlayerGame {
    @Id
    @Column(name = "c016_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "c002_id")
    private Game game;

    @Column(name = "c016_credit")
    private int credit;
}
