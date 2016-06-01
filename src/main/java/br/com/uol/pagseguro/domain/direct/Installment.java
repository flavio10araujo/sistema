package br.com.uol.pagseguro.domain.direct;

import java.math.BigDecimal;

/**
 * Represents the installment of a credit card payment
 */
public class Installment {

    /**
     * Quantity of installments
     */
    private final Integer quantity;

    /**
     * Value of each installment
     */
    private final BigDecimal value;

    /**
     * Initializes a new instance of the Installment class
     * 
     * @param quantity
     * @param value
     */
    public Installment(Integer quantity, BigDecimal value) {
        this.quantity = quantity;
        this.value = value;
    }

    /**
     * @return the quantity of installments
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @return the value of each installment
     */
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Installment[");
        sb.append("quantity=" + quantity);
        sb.append(",value=" + value);
        sb.append("]");
        return sb.toString();
    }

}
