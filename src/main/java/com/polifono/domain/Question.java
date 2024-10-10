package com.polifono.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t010_question")
public class Question {
    @Id
    @Column(name = "c010_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c010_name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "c010_order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "c009_id")
    private Content content;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @OrderBy("order asc")
    private List<Answer> answers;
}
