package com.polifono.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

@Setter
@Entity
@Table(name = "t017_diploma")
public class Diploma {
    @Getter
    @Id
    @Column(name = "c017_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(name = "c017_dt")
    private Date dt;

    @Getter
    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @Getter
    @ManyToOne
    @JoinColumn(name = "c002_id")
    private Game game;

    @Getter
    @ManyToOne
    @JoinColumn(name = "c003_id")
    private Level level;

    @Getter
    @Column(name = "c017_code")
    private String code;

    @Transient
    private int qtdHours; // Quantity of hour to get this diploma.

    @Transient
    private String dtStr; // dt in dd/MM/yyyy format.

    public int getQtdHours() {
        if (this.getLevel() != null) {
            return switch (this.getLevel().getOrder()) {
                case 1, 2, 3, 4, 5 -> 15;
                default -> 0;
            };
        } else {
            return 0;
        }
    }

    public String getDtStr() {
        return this.getDt() != null ? DateTimeFormatter.ofPattern("dd/MM/YYYY").format(ZonedDateTime.ofInstant(this.getDt().toInstant(), ZoneId.systemDefault())) : "";
    }
}
