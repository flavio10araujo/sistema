package com.polifono.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t013_login")
public class Login {

	@Id
	@Column(name = "c013_id")
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "c001_id")
	private Player player;
	
	@Column(name = "c013_dt_login")
	private Date dtLogin;

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

	public Date getDtLogin() {
		return dtLogin;
	}

	public void setDtLogin(Date dtLogin) {
		this.dtLogin = dtLogin;
	}
}