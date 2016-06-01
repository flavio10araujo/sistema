package com.polifono.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t007_player_phase")
public class PlayerPhase {

	@Id
	@Column(name = "c007_id")
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "c001_id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "c005_id")
	private Phase phase;
	
	@ManyToOne
	@JoinColumn(name = "c006_id")
	private Phasestatus phasestatus;
	
	@Column(name = "c007_grade")
	private double grade;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "c007_dt_test")
	private Date dtTest;
	
	@Column(name = "c007_num_attempts")
	private int numAttempts;
	
	@Column(name = "c007_score")
	private int score;
	
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

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public Phasestatus getPhasestatus() {
		return phasestatus;
	}

	public void setPhasestatus(Phasestatus phasestatus) {
		this.phasestatus = phasestatus;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public Date getDtTest() {
		return dtTest;
	}

	public void setDtTest(Date dtTest) {
		this.dtTest = dtTest;
	}

	public int getNumAttempts() {
		return numAttempts;
	}

	public void setNumAttempts(int numAttempts) {
		this.numAttempts = numAttempts;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}