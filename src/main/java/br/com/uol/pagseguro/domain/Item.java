/*
 ************************************************************************
 Copyright [2011] [PagSeguro Internet Ltda.]

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

package br.com.uol.pagseguro.domain;

import java.math.BigDecimal;

/**
 * Represents a product/item in a transaction
 */
public class Item {

    /**
     * Product identifier, such as SKU
     */
    private String id;

    /**
     * Product description
     */
    private String description;

    /**
     * Quantity
     */
    private Integer quantity;

    /**
     * Product unit price
     */
    private BigDecimal amount;

    /**
     * Single unit weight, in grams
     */
    private Long weight;

    /**
     * Single unit shipping cost
     */
    private BigDecimal shippingCost;

    /**
     * Initializes a newly created instance of this type
     */
    public Item() {

    }

    /**
     * Initializes a newly created instance of this type with the specified arguments
     * 
     * @param id
     *            the product identifier
     * @param description
     *            the product description
     * @param quantity
     *            the product quantity
     * @param amount
     *            the product unit price
     */
    public Item(String id, String description, Integer quantity, BigDecimal amount) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
    }

    /**
     * Initializes a newly created instance of this type with the specified arguments
     * 
     * @param id
     *            the product identifier
     * @param description
     *            the product description
     * @param quantity
     *            the product quantity
     * @param amount
     *            the product unit price
     * @param weight
     *            the product weight, in grams
     * @param shippingCost
     *            the product unit shippingCost
     */
    public Item(String id, String description, Integer quantity, BigDecimal amount, Long weight, BigDecimal shippingCost) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
        this.weight = weight;
        this.shippingCost = shippingCost;
    }

    /**
     * @return the product identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     *            the product identifier to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the product description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     *            the product description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the product quantity
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * @param quantity
     *            the product quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the product unit price
     */
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * @param amount
     *            the product unit price to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the product unit weight, in grams
     */
    public Long getWeight() {
        return this.weight;
    }

    /**
     * @param weight
     *            the product unit weight, in grams, to set
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * @return the product unit shipping cost
     */
    public BigDecimal getShippingCost() {
        return this.shippingCost;
    }

    /**
     * @param shippingCost
     *            the product unit shipping cost to set
     */
    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

}
