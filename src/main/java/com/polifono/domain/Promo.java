package com.polifono.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtBegin() {
        return dtBegin;
    }

    public void setDtBegin(Date dtBegin) {
        this.dtBegin = dtBegin;
    }

    public Date getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(Date dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }
}
