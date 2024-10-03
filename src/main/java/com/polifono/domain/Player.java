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
        if (this.email == null || email.isEmpty()) {
            return "";
        }

        return MD5Util.md5Hex(email.toLowerCase());
    }

    public String getDtBirthStr() {
        return DateUtil.formatDate(this.dtBirth);
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
