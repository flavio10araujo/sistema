package com.polifono.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t009_content")
public class Content {
    @Id
    @Column(name = "c009_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c009_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "c009_order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "c005_id")
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "c008_id")
    private Contenttype contenttype;

    @Transient
    private String playerAnswers;
}
