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
@Table(name = "t014_class")
public class Class {
    @Id
    @Column(name = "c014_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @Column(name = "c014_dt_inc")
    private Date dtInc;

    @Column(name = "c014_active")
    private boolean active;

    @Column(name = "c014_name")
    private String name;

    @Column(name = "c014_description")
    private String description;

    @Column(name = "c014_school")
    private String school;

    @Column(name = "c014_year")
    private int year;

    @Column(name = "c014_semester")
    private int semester;

    @Column(name = "c014_grade")
    private String grade;
}
