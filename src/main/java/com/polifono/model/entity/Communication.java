package com.polifono.model.entity;

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
@Table(name = "t020_communication")
public class Communication {
    @Id
    @Column(name = "c020_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c020_dt_inc")
    private Date dtInc;

    @ManyToOne
    @JoinColumn(name = "c019_id")
    private Groupcommunication groupcommunication;

    @Column(name = "c020_title")
    private String title;

    @Column(name = "c020_message", columnDefinition = "TEXT")
    private String message;
}
