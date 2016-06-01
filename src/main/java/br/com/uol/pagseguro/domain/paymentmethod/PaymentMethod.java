package br.com.uol.pagseguro.domain.paymentmethod;

import br.com.uol.pagseguro.enums.PaymentMethodStatus;

/**
 * Payment Method
 */
public class PaymentMethod implements Comparable<PaymentMethod> {

    private final int code;

    private final String name;

    private final String displayName;

    private final PaymentMethodStatus status;

    /**
     * Initializes a new instance of the PaymentMethod class
     * 
     * @param code
     * @param name
     * @param displayName
     * @param status
     */
    public PaymentMethod(int code, String name, String displayName, PaymentMethodStatus status) {
        if (name == null || displayName == null || status == null) {
            throw new IllegalArgumentException();
        }
        this.code = code;
        this.name = name;
        this.displayName = displayName;
        this.status = status;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the status
     */
    public PaymentMethodStatus getStatus() {
        return status;
    }

    public int compareTo(PaymentMethod other) {
        if (other == null) {
            return -1;
        }
        return code - other.getCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PaymentMethod[");
        sb.append("code=" + code);
        sb.append(",name=" + name);
        sb.append(",displayName=" + displayName);
        sb.append(",status=" + status);
        sb.append("]");
        return sb.toString();
    }

}
