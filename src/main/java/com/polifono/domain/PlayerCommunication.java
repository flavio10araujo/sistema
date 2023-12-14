package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t021_player_communication")
public class PlayerCommunication {

    @Id
    @Column(name = "c021_id")
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "c020_id")
    private Communication communication;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }
}
