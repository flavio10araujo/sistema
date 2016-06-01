package br.com.uol.pagseguro.domain.direct.checkout;

import java.util.Map;

import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Document;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.direct.Holder;
import br.com.uol.pagseguro.domain.direct.Installment;
import br.com.uol.pagseguro.enums.DocumentType;

/**
 * Represents the checkout of the credit card
 */
public class CreditCardCheckout extends Checkout {
    /**
     * Token
     */
    private String creditCardToken;

    /**
     * Installment
     */
    private Installment installment;

    /**
     * Holder
     */
    private Holder holder;

    /**
     * Billing address
     */
    private Address billingAddress;

    /**
     * Presencial
     */
    private Boolean presencial;

    /**
     * Dynamic payment method message
     */
    private String dynamicPaymentMethodMessage;

    /**
     * Initializes a new instance of the CreditCardCheckout class
     */
    public CreditCardCheckout() {

    }

    /**
     * @return the credit card token
     */
    public String getCreditCardToken() {
        return creditCardToken;
    }

    /**
     * @param creditCardToken
     *            the credit card token to set
     */
    public void setCreditCardToken(String creditCardToken) {
        this.creditCardToken = creditCardToken;
    }

    /**
     * @return the installment
     */
    public Installment getInstallment() {
        return installment;
    }

    /**
     * @param installment
     *            the installment to set
     */
    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    /**
     * @return the holder
     */
    public Holder getHolder() {
        return holder;
    }

    /**
     * @param holder
     *            the holder to set
     */
    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    /**
     * @return the billingAddress
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * @param billingAddress
     *            the billingAddress to set
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
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

    /**
     * @return the presencial
     */
    public Boolean getPresencial() {
        return presencial;
    }

    /**
     * @param presencial
     *            the presencial to set
     */
    public void setPresencial(Boolean presencial) {
        this.presencial = presencial;
    }

    @Override
    public Map<Object, Object> getMap() {
        final Map<Object, Object> data = super.getMap();

        data.put("paymentMethod", "CREDIT_CARD");

        if (creditCardToken != null) {
            data.put("creditCardToken", creditCardToken);
        }

        /**
         * @see Installment
         */
        if (installment != null) {
            if (installment.getQuantity() != null) {
                data.put("installmentQuantity", installment.getQuantity());
            }

            if (installment.getValue() != null) {
                data.put("installmentValue", installment.getValue());
            }
        }

        /**
         * @see Holder
         */
        if (holder != null) {
            if (holder.getName() != null) {
                data.put("creditCardHolderName", holder.getName());
            }

            /**
             * @see Phone
             */
            final Phone phone = holder.getPhone();
            if (phone != null) {
                if (phone.getAreaCode() != null) {
                    data.put("creditCardHolderAreaCode", phone.getAreaCode());
                }

                if (phone.getNumber() != null) {
                    data.put("creditCardHolderPhone", phone.getNumber());
                }
            }

            /**
             * @see Document
             */
            final Document document = holder.getDocument();
            if (document != null) {
                if (DocumentType.CPF.equals(document.getType()) && document.getValue() != null) {
                    data.put("creditCardHolderCPF", document.getValue());
                } else if (DocumentType.CNPJ.equals(document.getType()) && document.getValue() != null) {
                    data.put("creditCardHolderCNPJ", document.getValue());
                }
            }

            if (holder.getBirthDate() != null) {
                data.put("creditCardHolderBirthDate", holder.getBirthDate());
            }
        }

        /**
         * @see Address
         */
        if (billingAddress != null) {
            if (billingAddress.getPostalCode() != null) {
                data.put("billingAddressPostalCode", billingAddress.getPostalCode());
            }

            if (billingAddress.getStreet() != null) {
                data.put("billingAddressStreet", billingAddress.getStreet());
            }

            if (billingAddress.getNumber() != null) {
                data.put("billingAddressNumber", billingAddress.getNumber());
            }

            if (billingAddress.getComplement() != null) {
                data.put("billingAddressComplement", billingAddress.getComplement());
            }

            if (billingAddress.getDistrict() != null) {
                data.put("billingAddressDistrict", billingAddress.getDistrict());
            }

            if (billingAddress.getCity() != null) {
                data.put("billingAddressCity", billingAddress.getCity());
            }

            if (billingAddress.getState() != null) {
                data.put("billingAddressState", billingAddress.getState());
            }

            if (billingAddress.getCountry() != null) {
                data.put("billingAddressCountry", billingAddress.getCountry());
            }
        }

        if (presencial != null) {
            data.put("presencial", presencial);
        }

        if (dynamicPaymentMethodMessage != null) {
            data.put("dynamicPaymentMethodMessageCreditCard", dynamicPaymentMethodMessage);
        }

        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CreditCardCheckout[");
        sb.append("paymentMode=" + getPaymentMode());
        sb.append(",reference=" + getReference());
        sb.append(",senderEmail=" + getSender() != null ? getSender().getEmail() : null);
        sb.append("]");
        return sb.toString();
    }
}
