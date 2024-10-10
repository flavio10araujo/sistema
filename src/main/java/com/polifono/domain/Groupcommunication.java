package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t019_groupcommunication")
public class Groupcommunication {
    @Id
    @Column(name = "c019_id")
    private int id;
}
