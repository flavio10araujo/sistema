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
@Table(name = "t011_answer")
public class Answer {
    @Id
    @Column(name = "c011_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c011_name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "c011_order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "c010_id")
    private Question question;

    @Column(name = "c011_right")
    private boolean right;
}
