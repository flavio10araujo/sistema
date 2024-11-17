package com.polifono.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t012_transaction")
public class Transaction {
    @Id
    @Column(name = "c012_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @Column(name = "c012_quantity")
    private int quantity;

    @Column(name = "c012_dt_inc")
    private Date dtInc;

    @Column(name = "c012_closed")
    private boolean closed;

    @Column(name = "c012_code")
    private String code;

    @Column(name = "c012_reference")
    private String reference;

    @Column(name = "c012_date")
    private Date date;

    @Column(name = "c012_lasteventdate")
    private Date lastEventDate;

    @Column(name = "c012_type")
    private int type;

    @Column(name = "c012_status")
    private int status;

    @Column(name = "c012_paymentmethodtype")
    private int paymentMethodType;

    @Column(name = "c012_paymentmethodcode")
    private int paymentMethodCode;

    @Column(name = "c012_grossamount")
    private BigDecimal grossAmount;

    @Column(name = "c012_discountamount")
    private BigDecimal discountAmount;

    @Column(name = "c012_feeamount")
    private BigDecimal feeAmount;

    @Column(name = "c012_netamount")
    private BigDecimal netAmount;

    @Column(name = "c012_extraamount")
    private BigDecimal extraAmount;

    @Column(name = "c012_installmentcount")
    private int installmentCount;

    @Column(name = "c012_itemcount")
    private int itemCount;

    @Column(name = "c012_escrowenddate")
    private Date escrowEndDate;

    @Column(name = "c012_cancellationsource")
    private String cancellationSource;

    @Column(name = "c012_paymentlink")
    private String paymentLink;
}
