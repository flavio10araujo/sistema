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
@Table(name = "t005_phase")
public class Phase {
    @Id
    @Column(name = "c005_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c005_name")
    private String name;

    @Column(name = "c005_order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "c004_id")
    private Map map;

    @Transient
    boolean opened = false;
}
