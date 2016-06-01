package br.com.uol.pagseguro.domain.direct;

import java.util.Map;

import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.direct.checkout.CreditCardCheckout;

/**
 * Represents the payment request of the credit card
 * 
 * @deprecated use {@link CreditCardCheckout} instead.
 */
@Deprecated
public class CreditCardPaymentRequest extends PaymentRequest {

    /**
     * New CreditCardCheckout class
     */
    private CreditCardCheckout creditCardCheckout;

    /**
     * Token
     */
    @SuppressWarnings("unused")
    private String creditCardToken;

    /**
     * Installment
     */
    @SuppressWarnings("unused")
    private Installment installment;

    /**
     * Holder
     */
    @SuppressWarnings("unused")
    private Holder holder;

    /**
     * Billing address
     */
    @SuppressWarnings("unused")
    private Address billingAddress;

    /**
     * Presencial
     */
    @SuppressWarnings("unused")
    private Boolean presencial;

    /**
     * Dynamic payment method message
     */
    @SuppressWarnings("unused")
    private String dynamicPaymentMethodMessage;

    /**
     * Initializes a new instance of the PaymentRequestWithCreditCard class
     */
    public CreditCardPaymentRequest() {
        this.creditCardCheckout = new CreditCardCheckout();
    }

    /**
     * @return the credit card token
     */
    public String getCreditCardToken() {
        return this.creditCardCheckout.getCreditCardToken();
    }

    /**
     * @param creditCardToken
     *            the credit card token to set
     */
    public void setCreditCardToken(String creditCardToken) {
        this.creditCardCheckout.setCreditCardToken(creditCardToken);
    }

    /**
     * @return the installment
     */
    public Installment getInstallment() {
        return this.creditCardCheckout.getInstallment();
    }

    /**
     * @param installment
     *            the installment to set
     */
    public void setInstallment(Installment installment) {
        this.creditCardCheckout.setInstallment(installment);
    }

    /**
     * @return the holder
     */
    public Holder getHolder() {
        return this.creditCardCheckout.getHolder();
    }

    /**
     * @param holder
     *            the holder to set
     */
    public void setHolder(Holder holder) {
        this.creditCardCheckout.setHolder(holder);
    }

    /**
     * @return the billingAddress
     */
    public Address getBillingAddress() {
        return this.creditCardCheckout.getBillingAddress();
    }

    /**
     * @param billingAddress
     *            the billingAddress to set
     */
    public void setBillingAddress(Address billingAddress) {
        this.creditCardCheckout.setBillingAddress(billingAddress);
    }

    /**
     * @return the dynamicPaymentMethodMessage
     */
    public String getDynamicPaymentMethodMessage() {
        return this.creditCardCheckout.getDynamicPaymentMethodMessage();
    }

    /**
     * @param dynamicPaymentMethodMessage
     *            the dynamicPaymentMethodMessage to set
     */
    public void setDynamicPaymentMethodMessage(String dynamicPaymentMethodMessage) {
        this.creditCardCheckout.setDynamicPaymentMethodMessage(dynamicPaymentMethodMessage);
    }

    /**
     * @return the presencial
     */
    public Boolean getPresencial() {
        return this.creditCardCheckout.getPresencial();
    }

    /**
     * @param presencial
     *            the presencial to set
     */
    public void setPresencial(Boolean presencial) {
        this.creditCardCheckout.setPresencial(presencial);
    }

    @Override
    public Map<Object, Object> getMap() {
        return this.creditCardCheckout.getMap();
    }

    @Override
    public String toString() {
        return this.creditCardCheckout.toString();
    }
}
