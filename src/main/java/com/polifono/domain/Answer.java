package com.polifono.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t011_answer")
public class Answer {

	@Id
	@Column(name = "c011_id")
	@GeneratedValue
	private int id;
	
	@Column(name="c011_name", columnDefinition="TEXT")
	private String name;
	
	@Column(name = "c011_order")
	private int order;
	
	@ManyToOne
	@JoinColumn(name = "c010_id")
	private Question question;
	
	@Column(name = "c011_right")
	private boolean right;

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

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
}