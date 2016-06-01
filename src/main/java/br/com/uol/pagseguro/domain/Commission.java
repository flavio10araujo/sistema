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
 * Represents a commision
 */
public class Commission {

    /**
     * Rate
     */
    private BigDecimal rate;

    /**
     * Description
     */
    private String description;

    /**
     * Initializes a new instance of the Commission class
     */
    public Commission() {
    }

    /**
     * Initializes a new instance of the Commission class
     * 
     * @param rate
     * @param description
     */
    public Commission(BigDecimal rate, String description) {
        this.rate = rate;
        this.description = description;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return this.rate;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the area code
     * 
     * @param areaCode
     */
    public void setAreaCode(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * Sets the description
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
