package com.polifono.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.polifono.util.DateUtil;

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
@Table(name = "t017_diploma")
public class Diploma {
    @Id
    @Column(name = "c017_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "c017_dt", columnDefinition = "DATE")
    private Date dt;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "c002_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "c003_id")
    private Level level;

    @Column(name = "c017_code")
    private String code;

    public int getQtdHours() {
        if (getLevel() == null) {
            return 0;
        }

        return switch (this.getLevel().getOrder()) {
            case 1, 2, 3, 4, 5 -> 15;
            default -> 0;
        };
    }

    public String getDtStr() {
        return DateUtil.formatDate(this.dt);
    }
}
