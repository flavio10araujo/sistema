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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtInc() {
        return dtInc;
    }

    public void setDtInc(Date dtInc) {
        this.dtInc = dtInc;
    }

    public Groupcommunication getGroupcommunication() {
        return groupcommunication;
    }

    public void setGroupcommunication(Groupcommunication groupcommunication) {
        this.groupcommunication = groupcommunication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
