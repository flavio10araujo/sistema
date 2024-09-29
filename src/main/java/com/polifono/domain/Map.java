package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t004_map")
public class Map {
    @Id
    @Column(name = "c004_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c004_name")
    private String name;

    @Column(name = "c004_order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "c002_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "c003_id")
    private Level level;

    @Transient
    private boolean levelCompleted = false;

    @Transient
    private boolean gameCompleted = false;
}
