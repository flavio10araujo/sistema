package com.polifono.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t012_transaction")
public class Transaction {

	@Id
	@Column(name = "c012_id")
	@GeneratedValue
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getDtInc() {
		return dtInc;
	}

	public void setDtInc(Date dtInc) {
		this.dtInc = dtInc;
	}

	public String getCode() {
		return code;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getLastEventDate() {
		return lastEventDate;
	}

	public void setLastEventDate(Date lastEventDate) {
		this.lastEventDate = lastEventDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(int paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	public int getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(int paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

	public BigDecimal getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(BigDecimal extraAmount) {
		this.extraAmount = extraAmount;
	}

	public int getInstallmentCount() {
		return installmentCount;
	}

	public void setInstallmentCount(int installmentCount) {
		this.installmentCount = installmentCount;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public Date getEscrowEndDate() {
		return escrowEndDate;
	}

	public void setEscrowEndDate(Date escrowEndDate) {
		this.escrowEndDate = escrowEndDate;
	}

	public String getCancellationSource() {
		return cancellationSource;
	}

	public void setCancellationSource(String cancellationSource) {
		this.cancellationSource = cancellationSource;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}
}