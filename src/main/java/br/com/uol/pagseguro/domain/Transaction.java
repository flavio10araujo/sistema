/*
 ************************************************************************
 Copyright [2011] [PagSeguro Internet Ltda.]

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
package br.com.uol.pagseguro.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.enums.TransactionType;

/**
 * Represents a PagSeguro transaction
 */
public class Transaction {

    private static final int MIN_VALUE = 0;

    /** Transaction code */
    private String code;
    
    /** A reference to associate the PagSeguro transaction to a transaction in your system. */
    private String reference;
    
    /** Transaction date */
    private Date date;
    
    /** Date the last notification about this transaction was sent */
    private Date lastEventDate;

    /** Transaction type */
    private TransactionType type;

    /** Transaction status */
    private TransactionStatus status;

    /** Transaction payment method */
    private PaymentMethod paymentMethod;

    /** Transaction gross amount */
    private BigDecimal grossAmount;

    /** Transaction discount amount */
    private BigDecimal discountAmount;

    /** Transaction fee amount */
    private BigDecimal feeAmount;

    /** Transaction net amount */
    private BigDecimal netAmount;

    /** Transaction extra/discount amount */
    private BigDecimal extraAmount;

    /** Transaction installment count */
    private Integer installmentCount;

    /** Transaction items count */
    private Integer itemCount;

    /** Transaction escrow end date */
    private Date escrowEndDate;

    /** Cancellation source */
    private String cancellationSource;

    /** Transaction payment link */
    private String paymentLink;

    /**
     * item/product list in this transaction
     * 
     * @see Item
     */
    private List<Item> items;

    /**
     * Payer information, who is sending money
     * 
     * @see Sender
     */
    private Sender sender;

    /**
     * Shipping information
     * 
     * @see Shipping
     */
    private Shipping shipping;

    /**
     * Initializes a new instance of the Transaction class
     */
    public Transaction() {

        this.date = new Date();
        this.paymentMethod = new PaymentMethod();
        this.items = new ArrayList<Item>();
        this.itemCount = Integer.valueOf(MIN_VALUE);
        this.sender = new Sender();
        this.shipping = new Shipping();

    }

    /**
     * Date the last notification about this transaction was sent
     * 
     * @return the last event date
     */
    public Date getLastEventDate() {
        return this.lastEventDate;
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
     * Get Cancellation Source
     * 
     * @return String
     */
    public String getCancellationSource() {
        return this.cancellationSource;
    }

    /**
     * Set Cancellation Source
     * 
     * @param cancellationSource
     */
    public void setCancellationSource(String cancellationSource) {
        this.cancellationSource = cancellationSource;
    }

    /**
     * Get Payment Link
     * 
     * @return String
     */
    public String getPaymentLink() {
        return this.paymentLink;
    }

    /**
     * Set Payment Link
     * 
     * @param paymentLink
     */
    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    /**
     * @return the transaction date
     */
    public Date getDate() {
        return this.date;
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
     * @param grossAmount
     */
    public void setGrossAmount(BigDecimal totalValue) {
        this.grossAmount = totalValue;
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
     * @return the fee amount
     */
    public BigDecimal getFeeAmount() {
        return this.feeAmount;
    }

    /**
     * Sets the fee amount
     * 
     * @param feeAmount
     */
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
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
    public List<Item> getItems() {
        return this.items;
    }

    /**
     * Sets the list of items/products in this transaction
     * 
     * @see Item
     * 
     * @param items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return the sender information
     * @see Sender
     */
    public Sender getSender() {
        return this.sender;
    }

    /**
     * Sets the sender information, who is sending money in this transaction
     * 
     * @see Sender
     * 
     * @param sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * @return the shipping information
     * @see Shipping
     */
    public Shipping getShipping() {
        return this.shipping;
    }

    /**
     * sets the shipping information for this transaction
     * 
     * @see Shipping
     * 
     * @param shipping
     */
    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    @Override
    public String toString() {

        String codeValue = "Transaction(Code=" + this.code;
        String dateValue = ",Date=" + this.date;
        String referenceValue = ",Reference=" + this.reference;
        String statusValue = ",Status=" + this.status.getValue().toString();
        String emailValue = ",Email=" + this.sender != null ? this.sender.getEmail() : null;
        String itemsValue = ",ItemsCount=" + this.itemCount;

        StringBuilder sb = new StringBuilder();
        sb.append(codeValue);
        sb.append(dateValue);
        sb.append(referenceValue);
        sb.append(statusValue);
        sb.append(emailValue);
        sb.append(itemsValue);
        sb.append(")");

        return sb.toString();

    }
}
