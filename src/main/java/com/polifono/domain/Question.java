package com.polifono.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "t010_question")
public class Question {

    @Id
    @Column(name = "c010_id")
    @GeneratedValue
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
