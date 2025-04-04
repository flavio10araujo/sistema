package br.com.uol.pagseguroV2.domain.direct;

import br.com.uol.pagseguroV2.domain.direct.checkout.BoletoCheckout;

import java.util.Map;

/**
 * Represents the payment request of the boleto
 *
 * @deprecated use {@link BoletoCheckout} instead.
 */
@Deprecated
public class BoletoPaymentRequest extends PaymentRequest {

    /**
     * New BoletoCheckout class
     */
    private BoletoCheckout boletoCheckout;

    /**
     * Dynamic payment method message
     */
    @SuppressWarnings("unused")
    private String dynamicPaymentMethodMessage;

    /**
     * Initializes a new instance of the PaymentRequestWithBoleto class
     */
    public BoletoPaymentRequest() {
        this.boletoCheckout = new BoletoCheckout();
    }

    /**
     * @return the dynamicPaymentMethodMessage
     */
    public String getDynamicPaymentMethodMessage() {
        return this.boletoCheckout.getDynamicPaymentMethodMessage();
    }

    /**
     * @param dynamicPaymentMethodMessage
     *            the dynamicPaymentMethodMessage to set
     */
    public void setDynamicPaymentMethodMessage(String dynamicPaymentMethodMessage) {
        this.boletoCheckout.setDynamicPaymentMethodMessage(dynamicPaymentMethodMessage);
    }

    @Override
    public Map<Object, Object> getMap() {
        return this.boletoCheckout.getMap();
    }

    @Override
    public String toString() {
        return this.boletoCheckout.toString();
    }
}
