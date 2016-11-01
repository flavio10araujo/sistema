package com.polifono.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.polifono.util.DateUtil;
import com.polifono.util.MD5Util;

@Entity
@Table(name = "t001_player")
public class Player {

	@Id
	@Column(name = "c001_id")
	@GeneratedValue
	private int id;
	
	@Column(name = "c001_dt_inc")
	private Date dtInc;
	
	@Column(name = "c001_active")
	private boolean active;
	
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
	
	@Transient
	private String dtBirthStr;
	
	@Column(name = "c001_address")
	private String address;
	
	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
	private List<PlayerGame> playerGameList;
	
	@ManyToOne
	@JoinColumn(name = "c001_id_creator")
	private Player creator;
	
	@Column(name = "c001_login")
	private String login;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String getEmailMD5() {
		
		if (this.email == null || "".equals(email)) {
			return "";
		}
		
		return MD5Util.md5Hex(email.toLowerCase());
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public String getDtBirthStr() {
		return DateUtil.formatDate(this.dtBirth);
	}

	public void setDtBirthStr(String dtBirthStr) {
		try {
			this.dtBirth = DateUtil.parseDate(dtBirthStr);
		}
		catch (Exception e) {
			this.dtBirth = null;
		}
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

	public List<PlayerGame> getPlayerGameList() {
		return playerGameList;
	}

	public void setPlayerGameList(List<PlayerGame> playerGameList) {
		this.playerGameList = playerGameList;
	}
	
	public Player getCreator() {
		return creator;
	}

	public void setCreator(Player creator) {
		this.creator = creator;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Return the total quantity of credits: credit + specificCredit.
	 * 
	 * @return
	 */
	public int getTotalCredit() {
		return getCredit() + getSpecifiCredit();
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	/**
	 * Return the quantity of specific credits.
	 * 
	 * @return
	 */
	public int getSpecifiCredit() {
		List<PlayerGame> list = getPlayerGameList();
		
		if (list == null) {
			return 0;
		}
		
		int specificCredit = 0;
		
		for (PlayerGame pg : list) {
			specificCredit = specificCredit + pg.getCredit();
		}
		
		return specificCredit;
	}
}