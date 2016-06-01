/*
 ************************************************************************
 Copyright [2014] [PagSeguro Internet Ltda.]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************
 */

package br.com.uol.pagseguro.domain.checkout;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.MetaData;
import br.com.uol.pagseguro.domain.MetaDataItem;
import br.com.uol.pagseguro.domain.Parameter;
import br.com.uol.pagseguro.domain.ParameterItem;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.SenderDocument;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.enums.InvalidURL;
import br.com.uol.pagseguro.enums.MetaDataItemKey;
import br.com.uol.pagseguro.enums.ShippingType;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.checkout.CheckoutService;

/**
 * Represents a checkout request
 */
public class Checkout {
    /**
     * Party that will be sending the money
     */
    private Sender sender;

    /**
     * Payment currency.
     */
    private Currency currency;

    /**
     * List of products/items in this checkout request
     */
    private List<Item> items;

    /**
     * Uri to where the PagSeguro checkout page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     */
    private String redirectURL;

    /**
     * Extra/discount amount to be added to the transaction total
     * 
     * Optional. This value can be used to add an extra charge to the transaction or provide a discount in the case
     * ExtraAmount is a negative value.
     */
    private BigDecimal extraAmount;

    /**
     * Reference code
     * 
     * Optional. You can use the reference code to store an identifier so you can associate the PagSeguro transaction to
     * a transaction in your system.
     */
    private String reference;

    /**
     * Shipping information associated with this checkout request
     */
    private Shipping shipping;

    /**
     * How long this payment request will remain valid, in seconds.
     * 
     * Optional. After this checkout request is submitted, the payment code returned will remain valid for the period
     * specified here.
     */
    private BigInteger maxAge;

    /**
     * How many times the payment redirect url returned by the payment web service can be accessed.
     * 
     * Optional. After this checkout request is submitted, the payment redirect url returned by the checkout web service
     * will remain valid for the number of uses specified here.
     */
    private BigInteger maxUses;

    /**
     * Determines for which url PagSeguro will send the order related notifications changes.
     * 
     * Optional. A new notification will be send to this url if any change happens in the transaction status. You can
     * use that for update the related order.
     */
    private String notificationURL;

    /**
     * Extra parameters that user can add to a PagSeguro checkout request
     * 
     * Optional.
     * 
     * @var MetaData
     */
    private MetaData metaData;

    /**
     * Extra parameters that user can add to a PagSeguro checkout request
     * 
     * Optional
     * 
     * @var PagSeguroParameter
     */
    private Parameter parameter;

    /**
     * Initializes a new instance of the Checkout class
     */
    public Checkout() {
    }

    /**
     * @return the sender
     * 
     *         Party that will be sending the Uri to where the PagSeguro checkout page should redirect the user after
     *         the payment information is processed. money
     */
    public Sender getSender() {
        return this.sender;
    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param sender
     * 
     * @see Sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     */
    public void setSender(String name, String email) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param number
     */
    public void setSender(String name, String email, Phone number) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(number);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param areaCode
     * @param number
     */
    public void setSender(String name, String email, String areaCode, String number) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(new Phone(areaCode, number));

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param areaCode
     * @param number
     * @param bornDate
     */
    public void setSender(String name, String email, String areaCode, String number, String bornDate) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(new Phone(areaCode, number));
        this.sender.setBornDate(bornDate);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param areaCode
     * @param number
     */
    public void setSender(String name, String email, Phone number, String bornDate) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(number);
        this.sender.setBornDate(bornDate);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param areaCode
     * @param number
     * @param documentType
     * @param documentValue
     */
    public void setSender(String name, String email, String areaCode, String number, DocumentType documentType,
            String documentValue) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(new Phone(areaCode, number));
        this.sender.addDocument(documentType, documentValue);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param number
     * @param documentType
     * @param documentValue
     */
    public void setSender(String name, String email, Phone number, DocumentType documentType, String documentValue) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(number);
        this.sender.addDocument(documentType, documentValue);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param areaCode
     * @param number
     * @param documentType
     * @param documentValue
     * @param bornDate
     */
    public void setSender(String name, String email, String areaCode, String number, DocumentType documentType,
            String documentValue, String bornDate) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(new Phone(areaCode, number));
        this.sender.addDocument(documentType, documentValue);
        this.sender.setBornDate(bornDate);

    }

    /**
     * Sets the Sender, party that will be sending the money
     * 
     * @param name
     *            the sender full name or at least first and surname
     * @param email
     *            the sender e-mail address
     * @param number
     * @param documentType
     * @param documentValue
     * @param bornDate
     */
    public void setSender(String name, String email, Phone number, DocumentType documentType, String documentValue,
            String bornDate) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setName(name);
        this.sender.setEmail(email);
        this.sender.setPhone(number);
        this.sender.addDocument(documentType, documentValue);
        this.sender.setBornDate(bornDate);

    }

    /**
     * Sets the Sender phone number, phone of the party that will be sending the money
     * 
     * @param areaCode
     * @param number
     */
    public void setSenderPhone(String areaCode, String number) {

        if (this.sender == null) {
            this.sender = new Sender();
        }

        this.sender.setPhone(new Phone(areaCode, number));

    }

    /**
     * @return the currency Example: BRL
     */
    public Currency getCurrency() {
        return this.currency;
    }

    /**
     * Sets the currency
     * 
     * @param currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the items/products list in this checkout request
     * @see Item
     */
    public List<Item> getItems() {

        if (this.items == null) {
            this.items = new ArrayList<Item>();
        }

        return this.items;

    }

    /**
     * Sets the items/products list in this checkout request
     * 
     * @see Item
     * 
     * @param items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Adds a new product/item in this checkout request
     * 
     * @see Item
     * 
     * @param id
     * @param description
     * @param quantity
     * @param amount
     * @param weight
     * @param shippingCost
     */
    public void addItem(String id, String description, Integer quantity, BigDecimal amount, Long weight, BigDecimal shippingCost) {
        this.getItems().add(new Item(id, description, quantity, amount, weight, shippingCost));
    }

    /**
     * Adds a new product/item in this checkout request
     * 
     * @see Item
     * 
     * @param item
     */
    public void addItem(Item item) {
        this.getItems().add(item);
    }

    /**
     * Uri to where the PagSeguro checkout page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     * 
     * @return the redirectURL
     */
    public String getRedirectURL() {
        return this.redirectURL;
    }

    /**
     * Sets the redirect URL
     * 
     * Uri to where the PagSeguro checkout page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     * 
     * @param redirectURL
     */
    public void setRedirectURL(String redirectURL) {
        this.redirectURL = this.verifyURLTest(redirectURL);
    }

    /**
     * This value can be used to add an extra charge to the transaction or provide a discount in the case ExtraAmount is
     * a negative value.
     * 
     * @return the extra amount
     */
    public BigDecimal getExtraAmount() {
        return this.extraAmount;
    }

    /**
     * Sets the extra amount This value can be used to add an extra charge to the transaction or provide a discount in
     * the case <b>extraAmount</b> is a negative value.
     * 
     * @param extraAmount
     */
    public void setExtraAmount(BigDecimal extraAmount) {
        this.extraAmount = extraAmount;
    }

    /**
     * @return the reference of this checkout request
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Sets the reference of this checkout request
     * 
     * @param reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the shipping information for this checkout request
     * @see Shipping
     */
    public Shipping getShipping() {
        return this.shipping;
    }

    /**
     * Sets the shipping information for this checkout request
     * 
     * @see Shipping
     * 
     * @param shipping
     */
    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    /**
     * Sets the shipping type for this checkout request
     * 
     * @see ShippingType
     * 
     * @param type
     */
    public void setShippingType(ShippingType type) {

        if (this.shipping == null) {
            this.shipping = new Shipping();
        }

        this.shipping.setType(type);

    }

    /**
     * Sets the value of the shipping. <br />
     * Use it when you calculate the value of the shipping.
     * 
     * @param cost
     */
    public void setShippingCost(BigDecimal cost) {

        if (this.shipping == null) {
            this.shipping = new Shipping();
        }

        this.shipping.setCost(cost);

    }

    /**
     * Sets the shipping information for this checkout request
     * 
     * @param type
     * @param country
     * @param state
     * @param city
     * @param district
     * @param postalCode
     * @param street
     * @param number
     * @param complement
     */
    public void setShipping(ShippingType type, String country, String state, String city, String district,
            String postalCode, String street, String number, String complement) {

        if (this.shipping == null) {
            this.shipping = new Shipping();
        }

        this.shipping.setType(type);
        this.shipping.setAddress(new Address(country, state, city, district, postalCode, street, number, complement));

    }

    /**
     * /** Sets the shipping information for this checkout request
     * 
     * @param type
     * @param country
     * @param state
     * @param city
     * @param district
     * @param postalCode
     * @param street
     * @param number
     * @param complement
     * @param cost
     */
    public void setShipping(ShippingType type, String country, String state, String city, String district,
            String postalCode, String street, String number, String complement, BigDecimal cost) {

        this.setShipping(type, country, state, city, district, postalCode, street, number, complement);
        this.shipping.setCost(cost);

    }

    /**
     * Sets the shipping address for this checkout request
     * 
     * @param country
     * @param state
     * @param city
     * @param district
     * @param postalCode
     * @param street
     * @param number
     * @param complement
     */
    public void setShippingAddress(String country, String state, String city, String district, String postalCode,
            String street, String number, String complement) {

        if (this.shipping == null) {
            this.shipping = new Shipping();
        }

        this.shipping.setAddress(new Address(country, state, city, district, postalCode, street, number, complement));

    }

    /**
     * Sets the shipping address for this checkout request
     * 
     * @param address
     */
    public void setShippingAddress(Address address) {

        if (this.shipping == null) {
            this.shipping = new Shipping();
        }

        this.shipping.setAddress(address);

    }

    /**
     * @return the max age of this checkout request
     * 
     *         After this checkout request is submitted, the payment code returned will remain valid for the period
     *         specified.
     */
    public BigInteger getMaxAge() {
        return this.maxAge;
    }

    /**
     * Sets the max age of this payment request After this checkout request is submitted, the payment code returned will
     * remain valid for the period specified here.
     * 
     * @param maxAge
     */
    public void setMaxAge(BigInteger maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * After this checkout request is submitted, the payment redirect uri returned by the checkout web service will
     * remain valid for the number of uses specified here.
     * 
     * @return the max uses configured for this checkout request
     */
    public BigInteger getMaxUses() {
        return this.maxUses;
    }

    /**
     * Sets the max uses of this checkout request
     * 
     * After this checkout request is submitted, the payment redirect uri returned by the checkout web service will
     * remain valid for the number of uses specified here.
     * 
     * @param maxUses
     */
    public void setMaxUses(BigInteger maxUses) {
        this.maxUses = maxUses;
    }

    /**
     * Get the notification status url
     * 
     * @return String
     */
    public String getNotificationURL() {
        return this.notificationURL;
    }

    /**
     * Sets the url that PagSeguro will send the new notifications statuses
     * 
     * @param notificationURL
     */
    public void setNotificationURL(String notificationURL) {
        this.notificationURL = verifyURLTest(notificationURL);
    }

    /**
     * Add document for sender documents list
     * 
     * @param document
     */
    public void addSenderDocument(SenderDocument document) {
        this.getSender().addDocument(document);
    }

    /**
     * Add document for sender documents list
     * 
     * @param type
     * @param value
     */
    public void addSenderDocument(DocumentType type, String value) {
        this.getSender().addDocument(type, value);
    }

    /**
     * Get MetaData
     * 
     * @return MetaData
     */
    public MetaData getMetaData() {

        if (this.metaData == null) {
            this.metaData = new MetaData();
        }

        return this.metaData;

    }

    /**
     * Sets metadata for PagSeguro checkout requests
     * 
     * @param MetaData
     *            metaData
     */
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Add extra information, grouped, on the checkout request.
     * 
     * @param key
     * @param value
     * @param group
     */
    public void addMetaDataItem(MetaDataItemKey key, String value, Integer group) {
        this.getMetaData().addItem(new MetaDataItem(key, value, group));
    }

    /**
     * Add extra information, not grouped, on the checkout request.
     * 
     * @param key
     * @param value
     */
    public void addMetaDataItem(MetaDataItemKey key, String value) {
        this.getMetaData().addItem(new MetaDataItem(key, value));
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

    /**
     * Add parameters in the checkout request.
     * 
     * See availables <a href="https://pagseguro.uol.com.br/v2/guia-de-integracao/api-de-pagamentos.html">parameters</a>
     * 
     * @param name
     * @param value
     */
    public void addParameter(String name, String value) {
        this.getParameter().addItem(new ParameterItem(name, value));
    }

    /**
     * Add indexed parameters in the checkout request.
     * 
     * See availables <a href="https://pagseguro.uol.com.br/v2/guia-de-integracao/api-de-pagamentos.html">parameters</a>
     * 
     * @param name
     * @param value
     * @param index
     */
    public void addIndexedParameter(String name, String value, Integer index) {
        this.getParameter().addItem(new ParameterItem(name, value, index));
    }

    /**
     * Calls the PagSeguro web service and register this request for payment
     * 
     * @param credentials
     * @return The URL to where the user needs to be redirected to in order to complete the payment process
     * @throws PagSeguroServiceException
     */
    public String register(Credentials credentials) throws PagSeguroServiceException {
        return this.register(credentials, false);
    }

    /**
     * Calls the PagSeguro web service and register this request for payment
     * 
     * @param credentials
     * @param onlyCheckoutCode
     * @return The checkout code
     * @throws PagSeguroServiceException
     */
    public String register(Credentials credentials, Boolean onlyCheckoutCode) throws PagSeguroServiceException {
        return CheckoutService.createCheckoutRequest(credentials, this, onlyCheckoutCode);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PaymentRequest(Reference=");
        sb.append(reference);
        sb.append(",SenderEmail=");
        sb.append(sender != null ? sender.getEmail() : null);
        sb.append(")");
        return sb.toString();
    }

    /**
     * Verify if the address of notificationURL or redirectURL is for tests and return empty
     * 
     * @param type
     *            url
     * @return type
     */
    public String verifyURLTest(String url) {

        String urlReturn = url;

        for (InvalidURL invalid : InvalidURL.values()) {
            if (url.toLowerCase().contains(invalid.getValue().toLowerCase())) {
                urlReturn = "";
            }
        }

        return urlReturn;

    }
}
