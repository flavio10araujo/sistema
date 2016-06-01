package br.com.uol.pagseguro.domain.direct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Commission;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.Parameter;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.domain.direct.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.PaymentMode;
import br.com.uol.pagseguro.enums.ShippingType;

/**
 * Represents the payment request
 * 
 * @deprecated use {@link Checkout} instead.
 */
@Deprecated
public abstract class PaymentRequest {

    /**
     * New Checkout class
     */
    private Checkout checkout;

    /**
     * Payment Mode
     */
    @SuppressWarnings("unused")
    private PaymentMode paymentMode;

    /**
     * Receiver E-Mail
     */
    @SuppressWarnings("unused")
    private String receiverEmail;

    /**
     * Currency
     */
    @SuppressWarnings("unused")
    private Currency currency;

    /**
     * Notification URL
     */
    @SuppressWarnings("unused")
    private String notificationURL;

    /**
     * Reference
     */
    @SuppressWarnings("unused")
    private String reference;

    /**
     * Sender
     */
    @SuppressWarnings("unused")
    private Sender sender;

    /**
     * Shipping
     */
    @SuppressWarnings("unused")
    private Shipping shipping;

    /**
     * Extra Amount
     */
    @SuppressWarnings("unused")
    private BigDecimal extraAmount;

    /**
     * Items
     */
    @SuppressWarnings("unused")
    private List<Item> items;

    /**
     * Commission
     */
    @SuppressWarnings("unused")
    private Commission commission;
    
    /**
     * Extra parameters that user can add to a PagSeguro checkout request
     * 
     * Optional
     * 
     * @var PagSeguroParameter
     */
    private Parameter parameter;

    /**
     * @return the payment mode
     */
    public PaymentMode getPaymentMode() {
        return this.checkout.getPaymentMode();
    }

    /**
     * @param paymentMode
     *            the payment mode to set
     */
    public void setPaymentMode(PaymentMode paymentMode) {
        this.checkout.setPaymentMode(paymentMode);
    }

    /**
     * @return the receiverEmail
     */
    public String getReceiverEmail() {
        return this.checkout.getReceiverEmail();
    }

    /**
     * @param receiverEmail
     *            the receiverEmail to set
     */
    public void setReceiverEmail(String receiverEmail) {
        this.checkout.setReceiverEmail(receiverEmail);
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return this.checkout.getCurrency();
    }

    /**
     * @param currency
     *            the currency to set
     */
    public void setCurrency(Currency currency) {
        this.checkout.setCurrency(currency);
    }

    /**
     * @return the notification URL
     */
    public String getNotificationURL() {
        return this.checkout.getNotificationURL();
    }

    /**
     * @param notificationUrl
     *            the notification URL to set
     */
    public void setNotificationURL(String notificationURL) {
        this.checkout.setNotificationURL(notificationURL);
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return this.checkout.getReference();
    }

    /**
     * @param reference
     *            the reference to set
     */
    public void setReference(String reference) {
        this.checkout.setReference(reference);
    }

    /**
     * @return the sender
     */
    public Sender getSender() {
        return this.checkout.getSender();
    }

    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(Sender sender) {
        this.checkout.setSender(sender);
    }

    /**
     * @return the shipping
     */
    public Shipping getShipping() {
        return this.checkout.getShipping();
    }

    /**
     * @param shipping
     *            the shipping to set
     */
    public void setShipping(Shipping shipping) {
        this.checkout.setShipping(shipping);
    }

    /**
     * Sets the shipping address for this direct payment request
     * 
     * @param address
     */
    public void setShippingAddress(Address address) {
        this.checkout.setShippingAddress(address);
    }

    /**
     * Sets the shipping type for this direct payment request
     * 
     * @see ShippingType
     * 
     * @param type
     */
    public void setShippingType(ShippingType type) {
        this.checkout.setShippingType(type);
    }

    /**
     * Sets the shipping cost for this direct payment request
     * 
     * @param cost
     */
    public void setShippingCost(BigDecimal cost) {
        this.checkout.setShippingCost(cost);
    }

    /**
     * @return the extraAmount
     */
    public BigDecimal getExtraAmount() {
        return this.checkout.getExtraAmount();
    }

    /**
     * @param extraAmount
     *            the extraAmount to set
     */
    public void setExtraAmount(BigDecimal extraAmount) {
        this.checkout.setExtraAmount(extraAmount);
    }

    /**
     * @return the items
     */
    public List<Item> getItems() {
        return this.checkout.getItems();
    }

    /**
     * @param items
     *            the items to set
     */
    public void setItems(List<Item> items) {
        this.checkout.setItems(items);
    }

    /**
     * Adds a new product/item in this direct payment request
     * 
     * @see Item
     * 
     * @param item
     */
    public void addItem(Item item) {
        this.checkout.addItem(item);
    }

    /**
     * @return the commission
     */
    public Commission getCommission() {
        return this.checkout.getCommission();
    }

    /**
     * @param commission
     *            the commission to set
     */
    public void setCommission(Commission commission) {
        this.checkout.setCommission(commission);
    }
    
    /**
     * @return the sender hash
     */
    public String getSenderHash() {
        return this.checkout.getSenderHash();
    }

    /**
     * @param senderHash
     *            the sender hash to set
     */
    public void setSenderHash(String senderHash) {
        this.checkout.setSenderHash(senderHash);
    }
    
    /**
     * Gets parameter for PagSeguro checkout requests
     * 
     * @return Parameter
     */
    public Parameter getParameter() {

        if (this.parameter == null) {
            this.parameter = new Parameter();
        }

        return this.parameter;

    }

    /**
     * Sets parameter for PagSeguro checkout requests
     * 
     * @param parameter
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Map<Object, Object> getMap() {
        return this.checkout.getMap();
    }

}
