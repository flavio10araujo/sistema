package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t002_game")
public class Game {
    @Id
    @Column(name = "c002_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c002_name")
    private String name;

    @Column(name = "c002_namelink")
    private String namelink;

    @Column(name = "c002_order")
    private int order;

    @Column(name = "c002_active")
    private boolean active;
}
