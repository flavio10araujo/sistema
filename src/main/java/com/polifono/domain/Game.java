package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t002_game")
public class Game {

    @Id
    @Column(name = "c002_id")
    @GeneratedValue
    private int id;

    @Column(name = "c002_name")
    private String name;

    @Column(name = "c002_namelink")
    private String namelink;

    @Column(name = "c002_order")
    private int order;

    @Column(name = "c002_active")
    private boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamelink() {
        return namelink;
    }

    public void setNamelink(String namelink) {
        this.namelink = namelink;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
