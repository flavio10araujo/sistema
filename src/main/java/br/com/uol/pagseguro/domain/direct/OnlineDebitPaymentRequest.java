package br.com.uol.pagseguro.domain.direct;

import java.util.Map;

import br.com.uol.pagseguro.domain.direct.checkout.OnlineDebitCheckout;

/**
 * Represents the payment request of the online debit
 * 
 * @deprecated use {@link OnlineDebitCheckout} instead.
 */
@Deprecated
public class OnlineDebitPaymentRequest extends PaymentRequest {

    /**
     * New OnlineDebitCheckout class
     */
    private OnlineDebitCheckout onlineDebitCheckout;

    /**
     * Bank name
     */
    @SuppressWarnings("unused")
    private String bankName;

    /**
     * Initializes a new instance of the PaymentRequestWithOnlineDebit class
     */
    public OnlineDebitPaymentRequest() {
        this.onlineDebitCheckout = new OnlineDebitCheckout();
    }

    /**
     * @return the bank name
     */
    public String getBankName() {
        return this.onlineDebitCheckout.getBankName();
    }

    /**
     * @param bankName
     *            the bank name to set
     */
    public void setBankName(String bankName) {
        this.onlineDebitCheckout.setBankName(bankName);
    }

    @Override
    public Map<Object, Object> getMap() {
        return this.onlineDebitCheckout.getMap();
    }

    @Override
    public String toString() {
        return this.onlineDebitCheckout.toString();
    }

}
