package br.com.uol.pagseguro.domain.direct.checkout;

import java.util.Map;

/**
 * Represents the checkout of the boleto
 */
public class BoletoCheckout extends Checkout {
    /**
     * Dynamic payment method message
     */
    private String dynamicPaymentMethodMessage;

    /**
     * Initializes a new instance of the BoletoCheckout class
     */
    public BoletoCheckout() {

    }

    /**
     * @return the dynamicPaymentMethodMessage
     */
    public String getDynamicPaymentMethodMessage() {
        return dynamicPaymentMethodMessage;
    }

    /**
     * @param dynamicPaymentMethodMessage
     *            the dynamicPaymentMethodMessage to set
     */
    public void setDynamicPaymentMethodMessage(String dynamicPaymentMethodMessage) {
        this.dynamicPaymentMethodMessage = dynamicPaymentMethodMessage;
    }

    @Override
    public Map<Object, Object> getMap() {
        final Map<Object, Object> data = super.getMap();

        data.put("paymentMethod", "BOLETO");

        if (dynamicPaymentMethodMessage != null) {
            data.put("dynamicPaymentMethodMessageBoleto", dynamicPaymentMethodMessage);
        }

        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BoletoCheckout[");
        sb.append("paymentMode=" + getPaymentMode());
        sb.append(",reference=" + getReference());
        sb.append(",senderEmail=" + getSender() != null ? getSender().getEmail() : null);
        sb.append("]");
        return sb.toString();
    }
}
