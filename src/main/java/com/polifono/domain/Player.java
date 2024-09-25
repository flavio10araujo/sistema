package com.polifono.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polifono.domain.enums.Rank;
import com.polifono.domain.enums.Role;
import com.polifono.util.DateUtil;
import com.polifono.util.MD5Util;

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
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t001_player")
public class Player {
    @Setter @Getter
    @Id
    @Column(name = "c001_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter @Getter
    @Column(name = "c001_dt_inc")
    private Date dtInc;

    @Setter @Getter
    @Column(name = "c001_active")
    private boolean active;

    @Setter @Getter
    @Column(name = "c001_email")
    private String email;

    @Setter @Getter
    @JsonIgnore
    @Column(name = "c001_password")
    private String password;

    @Setter @Getter
    @Column(name = "c001_name")
    private String name;

    @Getter @Setter
    @Column(name = "c001_last_name")
    private String lastName;

    @Transient
    private String fullName;

    @Setter @Getter
    @Column(name = "c001_score")
    private int score;

    @Setter @Getter
    @Column(name = "c001_credit")
    private int credit;

    @Getter @Setter
    @Column(name = "c001_coin")
    private int coin;

    @Column(name = "c001_role")
    private String role;

    @Getter @Setter
    @Column(name = "c001_ind_email_confirmed")
    private boolean indEmailConfirmed;

    @Getter @Setter
    @JsonIgnore
    @Column(name = "c001_email_confirmed")
    private String emailConfirmed;

    @Getter @Setter
    @JsonIgnore
    @Column(name = "c001_password_reset")
    private String passwordReset;

    @Setter @Getter
    @Column(name = "c001_phone")
    private String phone;

    // 1 = Male; 2 = Female.
    @Setter @Getter
    @Column(name = "c001_sex")
    private int sex;

    @Setter @Getter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "c001_dt_birth", columnDefinition = "DATE")
    private Date dtBirth;

    @Transient
    private String dtBirthStr;

    @Setter
    @Transient
    private int dtBirthDay;

    @Setter
    @Transient
    private int dtBirthMonth;

    @Setter
    @Transient
    private int dtBirthYear;

    @Setter @Getter
    @Column(name = "c001_address")
    private String address;

    @Getter @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List<PlayerGame> playerGameList;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "c001_id_creator")
    private Player creator;

    @Getter @Setter
    @Column(name = "c001_login")
    private String login;

    @Getter @Setter
    @Column(name = "c001_nationality")
    private String nationality;

    @Getter @Setter
    @Column(name = "c001_city_of_birth")
    private String cityOfBirth;

    @Getter @Setter
    @Column(name = "c001_doc_01")
    private String rg;

    @Getter @Setter
    @Column(name = "c001_doc_01_exp")
    private String rgOrgExp;

    @Getter @Setter
    @Column(name = "c001_about", columnDefinition = "TEXT")
    private String about;

    @Getter @Setter
    @Column(name = "c018_id")
    private Long idFacebook;

    public String getEmailMD5() {
        if (this.email == null || email.isEmpty()) {
            return "";
        }

        return MD5Util.md5Hex(email.toLowerCase());
    }

    public Role getRole() {
        return Role.valueOf(role);
    }

    public void setRole(Role role) {
        this.role = (role == null) ? null : role.toString();
    }

    public String getDtBirthStr() {
        return DateUtil.formatDate(this.dtBirth);
    }

    public void setDtBirthStr(String dtBirthStr) {
        try {
            this.dtBirth = DateUtil.parseDate(dtBirthStr);
        } catch (Exception e) {
            this.dtBirth = null;
        }
    }

    public int getDtBirthDay() {
        if (this.dtBirth == null) {
            return -1;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.dtBirth);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getDtBirthMonth() {
        if (this.dtBirth == null) {
            return -1;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.dtBirth);
        return cal.get(Calendar.MONTH);
    }

    public int getDtBirthYear() {
        if (this.dtBirth == null) {
            return -1;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.dtBirth);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Return the total quantity of credits: credit + specificCredit.
     */
    public int getTotalCredit() {
        return getCredit() + getSpecifiCredit();
    }

    /**
     * Return the quantity of specific credits.
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

    public String getFullName() {
        return this.getName() + " " + this.getLastName();
    }

    /**
     * From 0 to 1000 points = Level 1 = White
     * From 1001 to 3000 points = Level 2 = Yellow
     * From 3001 to 5000 points = Level 3 = Orange
     * From 5001 to 6500 points = Level 4 = Red
     * From 6501 to 9000 points = Level 5 = Purple
     * From 9001 to 14000 points = Level 6 = Brown
     * From 14001 to 18000 points = Level 7 = Black
     * From 18001 to 21000 points = Level 8 = Copper
     * From 21001 to 24000 points = Level 9 = Silver
     * From 24001 to infinity = Level 10 = Gold
     */
    public Rank getRank() {

        int score = this.getScore();

        if (score <= 1000) {
            return Rank.WHITE;
        } else if (score <= 3000) {
            return Rank.YELLOW;
        } else if (score <= 5000) {
            return Rank.ORANGE;
        } else if (score <= 6500) {
            return Rank.RED;
        } else if (score <= 9000) {
            return Rank.PURPLE;
        } else if (score <= 14000) {
            return Rank.BROWN;
        } else if (score <= 18000) {
            return Rank.BLACK;
        } else if (score <= 21000) {
            return Rank.COPPER;
        } else if (score <= 24000) {
            return Rank.SILVER;
        } else {
            return Rank.GOLD;
        }
    }

    public String getRankColor() {
        return getRank().getColor();
    }

    public int getRankLevel() {
        return getRank().getLevel();
    }
}
