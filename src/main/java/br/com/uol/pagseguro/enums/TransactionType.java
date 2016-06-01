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
 * Enum constants that represents a list of known transaction types
 */
public enum TransactionType {

    PAYMENT("PAYMENT", 1),

    TRANSFER("TRANSFER", 2),

    FUND_ADDITION("FUND ADDITION", 3),

    WITHDRAW("WITHDRAW", 4),

    CHARGE("CHARGE", 5),

    DONATION("DONATION", 6),

    BONUS("BONUS", 7),

    BONUS_REPASS("BONUS REPASS", 8),

    OPERATIONAL("OPERATIONAL", 9),

    POLITICAL_DONATION("POLITICAL DONATION", 10),

    UNKNOWN_TYPE("UNKNOWN TYPE. SEE ONLINE DOCUMENTATION", -1);

    private String description;

    private Integer value;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param description
     *            - the type of the enum constant
     * @param value
     *            - the value of the enum constant
     */
    TransactionType(String description, Integer value) {
        this.description = description;
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
    public static TransactionType fromValue(Integer value) {

        for (TransactionType transactionType : values()) {
            if (transactionType.value.equals(value)) {
                return transactionType;
            }
        }

        UNKNOWN_TYPE.setValue(value);
        return UNKNOWN_TYPE;

    }

    /**
     * @return this enum constant description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            - new description for this enum constant
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return this enum constant value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value
     *            - new value for this enum constant
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}
