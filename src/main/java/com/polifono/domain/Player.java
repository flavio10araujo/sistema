package com.polifono.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polifono.domain.enums.Role;
import com.polifono.util.DateUtil;
import com.polifono.util.HashingUtil;
import com.polifono.util.RankUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "t001_player")
public class Player {
    @Id
    @Column(name = "c001_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c001_dt_inc")
    private Date dtInc;

    @Column(name = "c001_active")
    private boolean active;

    @Column(name = "c001_email")
    private String email;

    @JsonIgnore
    @Column(name = "c001_password")
    private String password;

    @Column(name = "c001_name")
    private String name;

    @Column(name = "c001_last_name")
    private String lastName;

    @Column(name = "c001_score")
    private int score;

    @Column(name = "c001_credit")
    private int credit;

    @Column(name = "c001_coin")
    private int coin;

    @Column(name = "c001_role")
    private String role;

    @Column(name = "c001_ind_email_confirmed")
    private boolean indEmailConfirmed;

    @JsonIgnore
    @Column(name = "c001_email_confirmed")
    private String emailConfirmed;

    @JsonIgnore
    @Column(name = "c001_password_reset")
    private String passwordReset;

    @Column(name = "c001_phone")
    private String phone;

    // 1 = Male; 2 = Female.
    @Column(name = "c001_sex")
    private int sex;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "c001_dt_birth", columnDefinition = "DATE")
    private Date dtBirth;

    @Column(name = "c001_address")
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List<PlayerGame> playerGameList;

    @ManyToOne
    @JoinColumn(name = "c001_id_creator")
    private Player creator;

    @Column(name = "c001_login")
    private String login;

    @Column(name = "c001_nationality")
    private String nationality;

    @Column(name = "c001_city_of_birth")
    private String cityOfBirth;

    @Column(name = "c001_doc_01")
    private String rg;

    @Column(name = "c001_doc_01_exp")
    private String rgOrgExp;

    @Column(name = "c001_about", columnDefinition = "TEXT")
    private String about;

    @Column(name = "c018_id")
    private Long idFacebook;

    public Role getRole() {
        return Role.valueOf(role);
    }

    public void setRole(Role role) {
        this.role = (role == null) ? null : role.toString();
    }

    /**
     * Used to get the www.gravatar.com/avatar/
     */
    public String getEmailMD5() {
        return HashingUtil.generateMD5Hash(email);
    }

    public String getDtBirthStr() {
        return DateUtil.formatDate(this.dtBirth);
    }

    public int getDtBirthDay() {
        return DateUtil.getDateField(this.dtBirth, Calendar.DAY_OF_MONTH);
    }

    public int getDtBirthMonth() {
        return DateUtil.getDateField(this.dtBirth, Calendar.MONTH);
    }

    public int getDtBirthYear() {
        return DateUtil.getDateField(this.dtBirth, Calendar.YEAR);
    }

    /**
     * Return the total quantity of credits: credit + specificCredit.
     */
    public int getTotalCredit() {
        return getCredit() + getSpecificCredit();
    }

    /**
     * Return the quantity of specific credits.
     */
    private int getSpecificCredit() {
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

    public String getFullName() {
        return this.getName() + " " + this.getLastName();
    }

    public String getRankColor() {
        return RankUtil.getRankByScore(score).getColor();
    }

    public int getRankLevel() {
        return RankUtil.getRankByScore(score).getLevel();
    }
}
