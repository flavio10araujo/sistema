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
 * Enum constants that represents a list of known transaction status
 */
public enum TransactionStatus {

    INITIATED("THE TRANSACTION HAS NOT BEEN COMPLETED", 0),

    WAITING_PAYMENT("THE PAYMENT HAS NOT YET BEEN PROCESSED", 1),

    IN_ANALYSIS("THE PAYMENT IS UNDER RISK REVIEW", 2),

    PAID("THE TRANSACTION PAYMENT HAS BEEN CONFIRMED", 3),

    AVAILABLE("THE TRANSACTION AMOUNT IS AVAILABLE FOR APPLICATION TO WITHDRAW", 4),

    IN_DISPUTE("THE TRANSACTION IS IN DISPUTE", 5),

    REFUNDED("THE TRANSACTION AMOUNT WAS RETURNED TO BUYER", 6),

    CANCELLED("THE TRANSACTION HAS BEEN CANCELLED", 7),

    SELLER_CHARGEBACK("THE PAYMENT WAS CONTESTED BY THE BUYER AND THE TRANSACTION AMOUNT PREVIOUSLY BLOCKED WAS DEBITED FROM YOUR BALANCE", 8),

    CONTESTATION("THE PAYMENT WAS CONTESTED BY THE BUYER AND THE TRANSACTION AMOUNT WAS BLOCKED. YOU MUST CONTACT OUR CUSTOMER SERVICE", 9),

    UNKNOWN_STATUS("UNKNOWN STATUS. SEE ONLINE DOCUMENTATION", -1);

    /**
     * Enum description
     */
    private String description;

    /**
     * Enum value
     */
    private Integer value;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param description
     *            the description of the enum constant
     * @param value
     *            the value of the enum constant
     */
    TransactionStatus(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    /**
     * Returns the enum constant of this type with the specified value. If a given value are not recognized return a
     * generic enum constant <code>UNKNOWN_STATUS</code>
     * 
     * @param value
     *            the value of the enum constant to be returned
     * @return the enum constant from a given value
     */
    public static TransactionStatus fromValue(Integer value) {

        for (TransactionStatus transactionStatus : values()) {
            if (transactionStatus.value.equals(value)) {
                return transactionStatus;
            }
        }

        UNKNOWN_STATUS.setValue(value);
        return UNKNOWN_STATUS;

    }

    /**
     * @return the enum constant description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     *            the description for this enum constant
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the enum constant value
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * @param value
     *            the value for this enum constant
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}
