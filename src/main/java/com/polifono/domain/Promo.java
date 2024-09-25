package com.polifono.domain;

import java.util.Date;

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
@Table(name = "t023_promo")
public class Promo {
    @Id
    @Column(name = "c023_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c023_dt_begin")
    private Date dtBegin;

    @Column(name = "c023_dt_end")
    private Date dtEnd;

    @Column(name = "c023_code")
    private String code;

    @Column(name = "c023_prize")
    private int prize;
}
