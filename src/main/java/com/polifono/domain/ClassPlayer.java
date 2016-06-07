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
@Table(name = "t015_class_player")
public class ClassPlayer {

	@Id
	@Column(name = "c015_id")
	@GeneratedValue
	private int id;

	@ManyToOne
	@JoinColumn(name = "c014_id")
	private Class clazz;
	
	@ManyToOne
	@JoinColumn(name = "c001_id")
	private Player player;
	
	@Column(name = "c015_dt_inc")
	private Date dtInc;
	
	@Column(name = "c015_active")
	private boolean active;
	
	@Column(name = "c015_dt_exc")
	private Date dtExc;
	
	@Column(name = "c015_status")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Date getDtInc() {
		return dtInc;
	}

	public void setDtInc(Date dtInc) {
		this.dtInc = dtInc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getDtExc() {
		return dtExc;
	}

	public void setDtExc(Date dtExc) {
		this.dtExc = dtExc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}