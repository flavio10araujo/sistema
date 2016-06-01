package br.com.uol.pagseguro.domain.installment;

import java.math.BigDecimal;

/**
 * Represents the option of installment of a credit card payment returned by PagSeguro API
 */
public class Installment implements Comparable<Installment> {

    /** Credit card brand */
    private final String cardBrand;

    /** Quantity of installments */
    private final int quantity;

    /** Value of each installment */
    private final BigDecimal amount;

    /** Total value of installments */
    private final BigDecimal totalAmount;

    /** Indicates if is an interest free transaction */
    private final boolean interestFree;

    /**
     * Initializes a new instance of the Installment class
     * 
     * @param cardBrand
     * @param quantity
     * @param amount
     * @param totalAmount
     * @param interestree
     */
    public Installment(String cardBrand, //
            int quantity, //
            BigDecimal amount, //
            BigDecimal totalAmount, //
            boolean interestFree) {
        if (cardBrand == null || amount == null || totalAmount == null) {
            throw new IllegalArgumentException();
        }
        this.cardBrand = cardBrand;
        this.quantity = quantity;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.interestFree = interestFree;
    }

    /**
     * @return the cardBrand
     */
    public String getCardBrand() {
        return cardBrand;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @return the interestFree
     */
    public boolean getInterestFree() {
        return interestFree;
    }

    public int compareTo(Installment other) {
        if (other == null) {
            return -1;
        }
        return quantity - other.getQuantity();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Installment[");
        sb.append("cardBrand=" + cardBrand);
        sb.append(",quantity=" + quantity);
        sb.append(",amount=" + amount);
        sb.append(",totalAmount=" + totalAmount);
        sb.append(",interestFree=" + interestFree);
        sb.append("]");
        return sb.toString();
    }

}
