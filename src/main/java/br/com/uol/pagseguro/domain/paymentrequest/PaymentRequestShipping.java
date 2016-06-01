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
 * Shipping information
 */
public class PaymentRequestShipping {

    /**
     * Shipping cost - fixed value
     * 
     * Optional if PaymentRequestShippingPackage not null
     */
    private BigDecimal cost;

    /**
     * Shipping cost - value to calculate
     * 
     * Optional if cost not null
     */
    private PaymentRequestShippingPackage paymentRequestShippingPackage;

    /**
     * Initializes a new instance of the PaymentRequestShipping class
     */
    public PaymentRequestShipping() {

    }

    /**
     * Initializes a new instance of the PaymentRequestShipping class with the specified arguments
     * 
     * @param cost
     */
    public PaymentRequestShipping(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * Initializes a new instance of the PaymentRequestShipping class with the specified arguments
     * 
     * @param paymentRequestShippingPackage
     */
    public PaymentRequestShipping(PaymentRequestShippingPackage paymentRequestShippingPackage) {
        this.paymentRequestShippingPackage = paymentRequestShippingPackage;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Sets the shipping cost (fixed value) of this shipping
     * 
     * @param cost
     *            the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the paymentRequestPackage
     */
    public PaymentRequestShippingPackage getPaymentRequestShippingPackage() {
        return paymentRequestShippingPackage;
    }

    /**
     * Sets the shipping cost (value to calculate) of this shipping
     * 
     * @param paymentRequestShippingPackage
     */
    public void setPaymentRequestPackage(PaymentRequestShippingPackage paymentRequestShippingPackage) {
        this.paymentRequestShippingPackage = paymentRequestShippingPackage;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "PaymentRequestShipping [cost=" + cost + ", paymentRequestShippingPackage="
                + paymentRequestShippingPackage + "]";
    }
}
