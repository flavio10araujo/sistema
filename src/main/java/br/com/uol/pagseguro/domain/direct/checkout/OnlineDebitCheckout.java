package br.com.uol.pagseguro.domain.direct.checkout;

import java.util.Map;

/**
 * Represents the checkout of the online debit
 */
public class OnlineDebitCheckout extends Checkout {

    /**
     * Bank name
     */
    private String bankName;

    /**
     * Initializes a new instance of the OnlineDebitCheckout class
     */
    public OnlineDebitCheckout() {

    }

    /**
     * @return the bank name
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName
     *            the bank name to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public Map<Object, Object> getMap() {
        final Map<Object, Object> data = super.getMap();

        data.put("paymentMethod", "ONLINE_DEBIT");

        if (bankName != null) {
            data.put("bankName", bankName);
        }

        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OnlineDebitCheckout[");
        sb.append("paymentMode=" + getPaymentMode());
        sb.append(",reference=" + getReference());
        sb.append(",senderEmail=" + getSender() != null ? getSender().getEmail() : null);
        sb.append("]");
        return sb.toString();
    }
}
