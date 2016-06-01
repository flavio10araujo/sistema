package com.polifono.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t009_content")
public class Content {

	@Id
	@Column(name = "c009_id")
	@GeneratedValue
	private int id;
	
	@Column(name="c009_content", columnDefinition="TEXT")
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public Contenttype getContenttype() {
		return contenttype;
	}

	public void setContenttype(Contenttype contenttype) {
		this.contenttype = contenttype;
	}

	public String getPlayerAnswers() {
		return playerAnswers;
	}

	public void setPlayerAnswers(String playerAnswers) {
		this.playerAnswers = playerAnswers;
	}
}