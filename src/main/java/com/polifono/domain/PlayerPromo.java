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
@Table(name = "t024_player_promo")
public class PlayerPromo {

	@Id
	@Column(name = "c024_id")
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "c001_id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "c023_id")
	private Promo promo;
	
	@Column(name = "c024_dt")
	private Date dt;

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

	public Promo getPromo() {
		return promo;
	}

	public void setPromo(Promo promo) {
		this.promo = promo;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}
}