package com.polifono.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t017_diploma")
public class Diploma {

	@Id
	@Column(name = "c017_id")
	@GeneratedValue
	private int id;

	@Column(name = "c017_dt")
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

	@Transient
	private int qtdHours; // Quantity of hour to get this diploma.
	
	@Transient
	private String dtStr; // dt in dd/MM/yyyy format.
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getQtdHours() {

		if (this.getLevel() != null) {
			if (this.getLevel().getOrder() == 1) {
				// 30 classes
				return 15;
			}
			else if (this.getLevel().getOrder() == 2) {
				// 60 classes
				return 15;
			}
			else if (this.getLevel().getOrder() == 3) {
				// 90 classes
				return 15;
			}
			else if (this.getLevel().getOrder() == 4) {
				// 120 classes
				return 15;
			}
			else if (this.getLevel().getOrder() == 5) {
				// 150 classes
				return 15;
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}

	public void setQtdHours(int qtdHours) {
		this.qtdHours = qtdHours;
	}

	public String getDtStr() {
		
		if (this.getDt() != null) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
			ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(this.getDt().toInstant(), ZoneId.systemDefault());
			return dateTimeFormatter.format(zonedDateTime);
		}
		else {
			return "";
		}
	}

	public void setDtStr(String dtStr) {
		this.dtStr = dtStr;
	}
}