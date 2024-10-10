package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t003_level")
public class Level {
    @Id
    @Column(name = "c003_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c003_name")
    private String name;

    @Column(name = "c003_order")
    private int order;

    @Column(name = "c003_active")
    private boolean active;

    @Transient
    private boolean opened = false;
}
