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

package br.com.uol.pagseguroV2.domain;

import br.com.uol.pagseguroV2.helper.PagSeguroUtil;

/**
 * Represents a phone number
 */
public class Phone {

    /**
     * Area code
     */
    private String areaCode;

    /**
     * Phone number
     */
    private String number;

    /**
     * Initializes a new instance of the Phone class
     */
    public Phone() {
    }

    /**
     * Initializes a new instance of the Phone class
     *
     * @param areaCode
     * @param number
     */
    public Phone(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    /**
     * @return the area code
     */
    public String getAreaCode() {
        return this.areaCode;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * Sets the area code
     *
     * @param areaCode
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * Sets the number
     *
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * return toString
     */
    @Override
    public String toString() {
        return this.areaCode + this.number;
    }

    /**
     * Sets the number from a formatted string
     *
     * @param number String formatted string like <code>(99) 9-9999-9999</code>
     */
    public void setFullPhone(String number) {

        number = PagSeguroUtil.removeCharacterPhone(number);
        Integer length = number.length();

        this.setAreaCode(number.substring(0, 2));
        this.setNumber(number.substring(2, length));
    }
}
