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
 * Payment request shipping package information
 */
public class PaymentRequestShippingPackage {

    /**
     * Shipping package width
     */
    private Integer width;

    /**
     * Shipping package height
     */
    private Integer height;

    /**
     * Shipping package length
     */
    private Integer length;

    /**
     * Shipping package weight
     */
    private BigDecimal weight;

    /**
     * Initializes a new instance of the PaymentRequestShippingPackage class
     */
    public PaymentRequestShippingPackage() {

    }

    /**
     * Initializes a new instance of the PaymentRequestShippingPackage class with the specified arguments
     *
     * @param width
     * @param height
     * @param length
     * @param weight
     */
    public PaymentRequestShippingPackage(Integer width, Integer height, Integer length, BigDecimal weight) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the package width
     * 
     * @param width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the package height
     * 
     * @param height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets the package length
     * 
     * @param length
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Sets the package weight
     * 
     * @param weight
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "PaymentRequestShippingPackage [width=" + width + ", height=" + height + ", length=" + length
                + ", weight=" + weight + "]";
    }
}
