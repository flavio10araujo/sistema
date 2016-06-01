package com.polifono.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t001_player")
public class Player {

	@Id
	@Column(name = "c001_id")
	@GeneratedValue
	private int id;
	
	@Column(name = "c001_dt_inc")
	private Date dtInc;
	
	@Column(name = "c001_email")
	private String email;
	
	@Column(name = "c001_password")
	private String password;
	
	@Column(name = "c001_name")
	private String name;
	
	@Column(name = "c001_score")
	private int score;
	
	@Column(name = "c001_credit")
	private int credit;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "c001_role")
    private Role role;
	
	@Column(name = "c001_ind_email_confirmed")
	private boolean indEmailConfirmed;
	
	@Column(name = "c001_email_confirmed")
	private String emailConfirmed;
	
	@Column(name = "c001_password_reset")
	private String passwordReset;
	
	@Column(name = "c001_phone")
	private String phone;
	
	// 1 = Male; 2 = Female.
	@Column(name = "c001_sex")
	private int sex;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "c001_dt_birth")
	private Date dtBirth;
	
	@Column(name = "c001_address")
	private String address;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDtInc() {
		return dtInc;
	}

	public void setDtInc(Date dtInc) {
		this.dtInc = dtInc;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Date getDtBirth() {
		return dtBirth;
	}

	public void setDtBirth(Date dtBirth) {
		this.dtBirth = dtBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isIndEmailConfirmed() {
		return indEmailConfirmed;
	}

	public void setIndEmailConfirmed(boolean indEmailConfirmed) {
		this.indEmailConfirmed = indEmailConfirmed;
	}

	public String getEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(String emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public String getPasswordReset() {
		return passwordReset;
	}

	public void setPasswordReset(String passwordReset) {
		this.passwordReset = passwordReset;
	}
}