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

package br.com.uol.pagseguro.enums;

/**
 * Enum constants that represents the currencies accepted by PagSeguro on payment requests
 */
public enum Currency {

    /** ISO 4217 */
    BRL("BRAZILIAN REAL", 986);

    private String name;

    private Integer num;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param name
     *            - the currency name
     * @param num
     *            - the currency code number
     */
    Currency(String name, Integer num) {
        this.name = name;
        this.num = num;
    }

    /**
     * @return this enum constant currency name
     */
    public String getCurrency() {
        return this.name;
    }

    /**
     * @param name
     *            - new currency name for this enum constant
     */
    public void setCurrency(String name) {
        this.name = name;
    }

    /**
     * @return this enum constant currency code number
     */
    public Integer getNum() {
        return this.num;
    }

    /**
     * @param num
     *            - new currency code number for this enum constant
     */
    public void setNum(Integer num) {
        this.num = num;
    }
}
