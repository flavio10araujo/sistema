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

package br.com.uol.pagseguro.domain.paymentrequest;

import java.math.BigDecimal;
import java.util.List;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.paymentrequest.PaymentRequestService;

/**
 * 
 * Represents a payment request transaction
 *
 */
public class PaymentRequest {

    /**
     * PaymentRequest name
     */
    private String name;

    /**
     * Payment request reference code
     * 
     * Optional
     */
    private String reference;

    /**
     * Party that will be sending the money
     */
    private Sender sender;

    /**
     * List of items in this payment request
     */
    private List<PaymentRequestItem> items;

    /**
     * Shipping information associated with this payment request
     */
    private PaymentRequestShipping shipping;

    /**
     * Description of this payment request/ Comment in the debtor email
     * 
     * Optional
     */
    private String description;

    /**
     * Expiration days of this payment request
     */
    private Integer expiration;

    /**
     * Due days of this payment request
     * 
     * Optional
     */
    private Integer due;

    /**
     * Initializes a new instance of the PaymentRequest class
     */
    public PaymentRequest() {

    }

    /**
     * Initializes a new instance of the PaymentRequest class with the specified arguments
     * 
     * @param name
     * @param reference
     * @param sender
     * @param items
     * @param shipping
     * @param description
     * @param expiration
     * @param due
     */
    public PaymentRequest(String name, String reference, Sender sender, List<PaymentRequestItem> items,
            PaymentRequestShipping shipping, String description, Integer expiration, Integer due) {
        super();
        this.name = name;
        this.reference = reference;
        this.sender = sender;
        this.items = items;
        this.shipping = shipping;
        this.description = description;
        this.expiration = expiration;
        this.due = due;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the payment request name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the payment request reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the payment request reference
     * 
     * @param reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the PaymentRequestSender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Sets the PaymentRequestSender
     * 
     * @param sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * @return the items
     */
    public List<PaymentRequestItem> getItems() {
        return items;
    }

    /**
     * Sets the payment request items
     * 
     * @param items
     */
    public void setItems(List<PaymentRequestItem> items) {
        this.items = items;
    }

    /**
     * Adds a new item in this payment request
     * 
     * @see Item
     * 
     * @param id
     * @param description
     * @param amount
     * @param quantity
     */
    public void addItem(String id, String description, BigDecimal amount, Integer quantity) {
        this.getItems().add(new PaymentRequestItem(id, description, amount, quantity));
    }

    /**
     * Adds a new item in this payment request
     * 
     * @see Item
     * 
     * @param item
     */
    public void addItem(PaymentRequestItem item) {
        this.getItems().add(item);
    }

    /**
     * @return the shipping
     */
    public PaymentRequestShipping getShipping() {
        return shipping;
    }

    /**
     * Sets the payment request shipping
     * 
     * @param shipping
     */
    public void setShipping(PaymentRequestShipping shipping) {
        this.shipping = shipping;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the payment request description
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the expiration
     */
    public Integer getExpiration() {
        return expiration;
    }

    /**
     * Sets the payment request expiration days
     * 
     * @param expiration
     */
    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    /**
     * @return the due days
     */
    public Integer getDue() {
        return due;
    }

    /**
     * Sets the payment request due days
     * 
     * @param due
     */
    public void setDue(Integer due) {
        this.due = due;
    }

    /**
     * Calls the PagSeguro web service and register this payment request
     * 
     * @param credentials
     * @return The payment request code
     * @throws PagSeguroServiceException
     */
    public String register(Credentials credentials) throws PagSeguroServiceException {
        return PaymentRequestService.createPaymentRequest(credentials, this);
    }

    /**
     * Calls the PagSeguro web service and return a payment request
     * 
     * @param credentials
     * @param paymentRequestCode
     * @return The payment request
     * @throws PagSeguroServiceException
     */
    public PaymentRequestTransaction search(Credentials credentials, String paymentRequestCode)
            throws PagSeguroServiceException {
        return PaymentRequestService.findByCode(credentials, paymentRequestCode);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder()//
                .append("PaymentRequest [")//
                .append("name=\"")//
                .append(name + "\"")//
                .append(",reference=\"")//
                .append(reference + "\"")//
                .append(",sender=")//
                .append(sender)//
                .append(",items=")//
                .append(items)//
                .append(",shipping=")//
                .append(shipping)//
                .append(",description=\"")//
                .append(description + "\"")//
                .append(",expiration=")//
                .append(expiration)//
                .append(",due=")//
                .append(due)//
                .append("]");
        return builder.toString();
    }
}
