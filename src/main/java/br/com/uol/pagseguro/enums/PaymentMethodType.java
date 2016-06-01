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
 * Enum constants that represents a list of known paymentMethodTypes
 */
public enum PaymentMethodType {

    CREDIT_CARD("CREDIT CARD", 1),

    BOLETO("BANK PAYMENT ORDER", 2),

    ONLINE_TRANSFER("WIRE TRANSFER", 3),

    BALANCE("PAGSEGURO BALANCE", 4),

    OI_PAGGO("OI PAGGO", 5),

    DIRECT_DEPOSIT("DIRECT DEPOSIT", 7),

    UNKNOWN_TYPE("UNKNOWN TYPE. SEE ONLINE DOCUMENTATION", -1);

    private String type;

    private Integer value;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param type
     *            - the type of the enum constant
     * @param value
     *            - the value of the enum constant
     */
    PaymentMethodType(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the enum constant of this type with the specified value. If a given value are not recognized return a
     * generic enum constant <code>UNKNOWN_TYPE</code>
     * 
     * @param value
     *            - the value of the enum constant to be returned
     * @return the enum constant from a given value
     */
    public static PaymentMethodType fromValue(Integer value) {

        for (PaymentMethodType paymentMethodType : values()) {
            if (paymentMethodType.value.equals(value)) {
                return paymentMethodType;
            }
        }

        UNKNOWN_TYPE.setValue(value);
        return UNKNOWN_TYPE;

    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}
