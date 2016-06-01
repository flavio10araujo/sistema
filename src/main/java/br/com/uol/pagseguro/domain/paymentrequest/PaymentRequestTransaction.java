/*
 ************************************************************************
 Copyright [2014] [PagSeguro Internet Ltda.]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************
 */

package br.com.uol.pagseguro.domain.paymentrequest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.uol.pagseguro.domain.AutomaticDebit;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.PaymentMethod;
import br.com.uol.pagseguro.domain.Receiver;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.enums.TransactionType;

/**
 * Represents a PagSeguro payment request transaction
 */

public class PaymentRequestTransaction {

    private static final int MIN_VALUE = 0;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /** Transaction date */
    private Date date;

    /** Transaction code */
    private String code;

    /** Transaction recurrence code */
    private String recurrenceCode;

    /** A reference to associate the PagSeguro transaction to a transaction in your system. */
    private String reference;

    /** Recovery code of the initiated transaction. */
    private String recoveryCode;

    /** Transaction type */
    private TransactionType type;

    /** Payment request type */
    private PaymentRequestType paymentRequestType;

    /** Cancellation source */
    private String cancellationSource;

    /** Automatic debit information */
    private AutomaticDebit automaticDebit;

    /** Transaction status */
    private TransactionStatus status;

    /** Date the last notification about this transaction was sent */
    private Date lastEventDate;

    /** Transaction payment method */
    private PaymentMethod paymentMethod;

    /** Transaction gross amount */
    private BigDecimal grossAmount;

    /** Transaction paid amount */
    private BigDecimal paidAmount;

    /** Transaction discount amount */
    private BigDecimal discountAmount;

    /** Transaction receiver fees */
    private PaymentRequestReceiverFees receiverFees;

    /** Transaction net amount */
    private BigDecimal netAmount;

    /** Transaction extra/discount amount */
    private BigDecimal extraAmount;

    /** Transaction description */
    private String description;

    /** Receiver information */
    private Receiver receiver;

    /** Transaction escrow end date */
    private Date escrowEndDate;

    /** Transaction installment count */
    private Integer installmentCount;

    /** Transaction items count */
    private Integer itemCount;

    /**
     * item/product list in this transaction
     * 
     * @see Item
     */
    private List<PaymentRequestItem> items;

    /**
     * Payer information, who is sending money
     * 
     * @see PaymentRequestSender
     */
    private PaymentRequestSender sender;

    /**
     * Shipping information
     * 
     * @see PaymentRequestShipping
     */
    private PaymentRequestShipping shipping;

    /** Transaction expiration */
    private Date expiration;

    /** Transaction due */
    private Date due;

    private SimpleDateFormat sDF;

    /**
     * Initializes a new instance of the PaymentRequestTransaction class
     */
    public PaymentRequestTransaction() {

        this.date = new Date();
        this.paymentMethod = new PaymentMethod();
        this.receiverFees = new PaymentRequestReceiverFees();
        this.items = new ArrayList<PaymentRequestItem>();
        this.itemCount = Integer.valueOf(MIN_VALUE);
        this.sender = new PaymentRequestSender();
        this.shipping = new PaymentRequestShipping();
        this.automaticDebit = new AutomaticDebit();
        this.receiver = new Receiver();
        this.sDF = new SimpleDateFormat(DATE_FORMAT);

    }

    /**
     * @return the transaction date
     */
    public String getDate() {
        if(this.date == null) {
            return null;
        }

        return this.sDF.format(this.date);
    }

    /**
     * Sets the transaction date
     * 
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the transaction code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the transaction code
     * 
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the recurrenceCode
     */
    public String getRecurrenceCode() {
        return recurrenceCode;
    }

    /**
     * Sets the recurrence code
     * 
     * @param recurrenceCode
     */
    public void setRecurrenceCode(String recurrenceCode) {
        this.recurrenceCode = recurrenceCode;
    }

    /**
     * You can use the reference code to store an identifier so you can associate the PagSeguro transaction to a
     * transaction in your system.
     * 
     * @return the reference code
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference code
     * 
     * @param reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the recoveryCode
     */
    public String getRecoveryCode() {
        return recoveryCode;
    }

    /**
     * Ses the recovery code
     * 
     * @param recoveryCode
     */
    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    /**
     * @return the receiver
     */
    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * Ses the receiver
     *
     * @param receiver
     */
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the automatic debit information
     */
    public AutomaticDebit getAutomaticDebit() {
        return automaticDebit;
    }

    /**
     * Ses the automatic debit
     *
     * @param automaticDebit
     */
    public void setAutomaticDebit(AutomaticDebit automaticDebit) {
        this.automaticDebit = automaticDebit;
    }

    /**
     * @return the transaction Type
     * @see TransactionType
     */
    public TransactionType getType() {
        return this.type;
    }

    /**
     * Sets the transaction type
     * 
     * @see TransactionType
     * 
     * @param type
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * @return the transaction status
     * @see TransactionStatus
     */
    public TransactionStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the transaction status
     * 
     * @see TransactionStatus
     * 
     * @param status
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * Date the last notification about this transaction was sent
     * 
     * @return the last event date
     */
    public String getLastEventDate() {
        if(this.lastEventDate == null) {
            return null;
        }

        return this.sDF.format(this.lastEventDate);
    }

    /**
     * Sets the last event date
     * 
     * @param lastEventDate
     */
    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    /**
     * @return the payment method used in this transaction
     * @see PaymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    /**
     * Sets the payment method for this transaction
     * 
     * @param paymentMethod
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the transaction gross amount
     */
    public BigDecimal getGrossAmount() {
        return this.grossAmount;
    }

    /**
     * Sets the transaction gross amount
     * 
     * @param totalValue
     */
    public void setGrossAmount(BigDecimal totalValue) {
        this.grossAmount = totalValue;
    }

    /**
     * @return the paidAmount
     */
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    /**
     * @param paidAmount
     *            the paidAmount to set
     */
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    /**
     * @return the discount amount
     */
    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    /**
     * Sets the discount amount
     * 
     * @param discountAmount
     */
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * @return the receiverFees
     */
    public PaymentRequestReceiverFees getReceiverFees() {
        return receiverFees;
    }

    /**
     * @param receiverFees
     *            the receiverFees to set
     */
    public void setReceiverFees(PaymentRequestReceiverFees receiverFees) {
        this.receiverFees = receiverFees;
    }

    /**
     * @return the net amount
     */
    public BigDecimal getNetAmount() {
        return this.netAmount;
    }

    /**
     * Sets the net amount
     * 
     * @param netAmount
     */
    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    /**
     * @return the extra amount
     */
    public BigDecimal getExtraAmount() {
        return this.extraAmount;
    }

    /**
     * Sets the extra amount
     * 
     * @param extraAmount
     */
    public void setExtraAmount(BigDecimal extraAmount) {
        this.extraAmount = extraAmount;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get Escrow End Date
     * 
     * @return Date
     */
    public Date getEscrowEndDate() {
        return escrowEndDate;
    }

    /**
     * Set Escrow End Date
     * 
     * @param escrowEndDate
     */
    public void setEscrowEndDate(Date escrowEndDate) {
        this.escrowEndDate = escrowEndDate;
    }

    /**
     * @return the installment count
     */
    public Integer getInstallmentCount() {
        return this.installmentCount;
    }

    /**
     * Sets the installment count in this transaction
     * 
     * @param installmentCount
     */
    public void setInstallmentCount(Integer installmentCount) {
        this.installmentCount = installmentCount;
    }

    /**
     * Sets the items count
     * 
     * @param itemCount
     */
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * @return the items/products count in this transaction
     */
    public Integer getItemCount() {
        return this.itemCount;
    }

    /**
     * @return the items/products list in this transaction
     * @see Item
     */
    public List<PaymentRequestItem> getItems() {
        return this.items;
    }

    /**
     * Sets the list of items/products in this transaction
     * 
     * @see Item
     * 
     * @param items
     */
    public void setItems(List<PaymentRequestItem> items) {
        this.items = items;
    }

    /**
     * @return the sender information
     * @see PaymentRequestSender
     */
    public PaymentRequestSender getSender() {
        return this.sender;
    }

    /**
     * Sets the sender information, who is sending money in this transaction
     * 
     * @see PaymentRequestSender
     * 
     * @param sender
     */
    public void setSender(PaymentRequestSender sender) {
        this.sender = sender;
    }

    /**
     * @return the shipping information
     * @see Shipping
     */
    public PaymentRequestShipping getShipping() {
        return this.shipping;
    }

    /**
     * Sets the payment request type for this transaction
     * 
     * @see PaymentRequestType
     * 
     * @param paymentRequestType
     */
    public void setPaymentRequestType(PaymentRequestType paymentRequestType) {
        this.paymentRequestType = paymentRequestType;
    }

    /**
     * @return the payment request type
     * @see PaymentRequestType
     */
    public PaymentRequestType getPaymentRequestType() {
        return this.paymentRequestType;
    }

    /**
     * Sets the shipping information for this transaction
     *
     * @see PaymentRequestShipping
     *
     * @param shipping
     */
    public void setShipping(PaymentRequestShipping shipping) {
        this.shipping = shipping;
    }

    /**
     * @return the expiration
     */
    public String getExpiration() {
        if(this.expiration == null) {
            return null;
        }

        return this.sDF.format(this.expiration);
    }

    /**
     * Sets the expiration days
     * 
     * @param expiration
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * @return the due
     */
    public String getDue() {
        if(this.due == null) {
            return null;
        }

        return this.sDF.format(this.due);
    }

    /**
     * Sets the due days
     * 
     * @param due
     */
    public void setDue(Date due) {
        this.due = due;
    }

    public String getCancellationSource() {

        return cancellationSource;
    }

    /**
     * Sets the cancellation source
     *
     * @param cancellationSource
     */
    public void setCancellationSource(String cancellationSource) {

        this.cancellationSource = cancellationSource;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder()//
                .append("PaymentRequestTransaction [")//
                .append("date=")//
                .append(date)//
                .append(",code=\"")//
                .append(code + "\"")//
                .append(",recurrenceCode=\"")//
                .append(recurrenceCode + "\"")//
                .append(",reference=\"")//
                .append(reference + "\"")//
                .append(",recoveryCode=\"")//
                .append(recoveryCode + "\"")//
                .append(",type=")//
                .append(type)//
                .append(",paymentRequestType=")//
                .append(paymentRequestType)//
                .append(",cancellationSource=\"")//
                .append(cancellationSource + "\"")//
                .append(",automaticDebit=")//
                .append(automaticDebit)//
                .append(",status=")//
                .append(status)//
                .append(",lastEventDate=")//
                .append(lastEventDate)//
                .append(",paymentMethod=")//
                .append(paymentMethod)//
                .append(",grossAmount=")//
                .append(grossAmount)//
                .append(",paidAmount=")//
                .append(paidAmount)//
                .append(",discountAmount=")//
                .append(discountAmount)//
                .append(",receiverFees=")//
                .append(receiverFees)//
                .append(",netAmount=")//
                .append(netAmount)//
                .append(",extraAmount=")//
                .append(extraAmount)//
                .append(",description=\"")//
                .append(description + "\"")//
                .append(",receiver=")//
                .append(receiver)//
                .append(",escrowEndDate=")//
                .append(escrowEndDate)//
                .append(",installmentCount=")//
                .append(installmentCount)//
                .append(",itemCount=")//
                .append(itemCount)//
                .append(",items=")//
                .append(items)//
                .append(",sender=")//
                .append(sender)//
                .append(",shipping=")//
                .append(shipping)//
                .append(",expiration=")//
                .append(expiration)//
                .append(",due=")//
                .append(due)//
                .append("]");
        return builder.toString();
    }
}
