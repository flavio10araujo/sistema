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

/**
 * Represents an item in a payment request transaction
 */
public class PaymentRequestItem {

    /**
     * Item id
     * 
     * Optional
     */
    private String id;

    /**
     * Item description
     */
    private String description;

    /**
     * Item amount
     */
    private BigDecimal amount;

    /**
     * Item quantity
     */
    private Integer quantity;

    /**
     * Initializes a new instance of PaymentRequestItem class
     */
    public PaymentRequestItem() {

    }

    /**
     * Initializes a new instance of PaymentRequestItem class with the specified arguments
     * 
     * @param id
     *            the item id
     * @param description
     *            the item description
     * @param amount
     *            the item amount
     * @param quantity
     *            the item quantity
     */
    public PaymentRequestItem(String id, String description, BigDecimal amount, Integer quantity) {
        this.description = description;
        this.amount = amount;
        this.quantity = quantity;
        this.id = id;
    }

    /**
     * @return the item description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the item description
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the item amount
     */
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Sets the item amount
     * 
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the item quantity
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the item quantity
     * 
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the item id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the item id
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "PaymentRequestItem [id=" + id + ", description=" + description + ", amount=" + amount + ", quantity="
                + quantity + "]";
    }
}
