/*
 * ***********************************************************************
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
 * ***********************************************************************
 */

package br.com.uol.pagseguro.enums;

/**
 * Enum constants that represents a list of known paymenMethodCodes
 */
public enum PaymentMethodCode {

    VISA_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "VISA CREDIT CARD", 101),

    MASTERCARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "MASTERCARD CREDIT CARD", 102),

    AMEX_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "AMEX CREDIT CARD", 103),

    DINERS_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "DINERS CREDIT CARD", 104),

    HIPERCARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "HIPERCARD CREDIT CARD", 105),

    AURA_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "AURA CREDIT CARD", 106),

    ELO_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "ELO CREDIT CARD", 107),

    PLENOCARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "PLENOCARD CREDIT CARD", 108),

    PERSONALCARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "PERSONALCARD CREDIT CARD", 109),

    JCB_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "JCB CREDIT CARD", 110),

    DISCOVER_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "DISCOVER CREDIT CARD", 111),

    BRASILCARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "BRASILCARD CREDIT CARD", 112),

    FORTBRASIL_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "FORTBRASIL CREDIT CARD", 113),

    CARDBAN_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "CARDBAN CREDIT CARD", 114),

    VALECARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "VALECARD CREDIT CARD", 115),

    CABAL_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "CABAL CREDIT CARD", 116),

    MAIS_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "MAIS! CREDIT CARD", 117),

    AVISTA_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "AVISTA CREDIT CARD", 118),

    GRANDCARD_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "GRANDCARD CREDIT CARD", 119),

    SOROCRED_CREDIT_CARD(PaymentMethodType.CREDIT_CARD, "Sorocred CREDIT CARD", 120),

    BRADESCO_BOLETO(PaymentMethodType.BOLETO, "BRADESCO BOLETO", 201),

    SANTANDER_BOLETO(PaymentMethodType.BOLETO, "SANTANDER BOLETO", 202),

    BRADESCO_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "BRADESCO WIRE TRANSFER", 301),

    ITAU_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "ITAÚ WIRE TRANSFER", 302),

    UNIBANCO_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "UNIBANCO WIRE TRANSFER", 302),

    BANCO_BRASIL_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "BANCO DO BRASIL WIRE TRANSFER", 304),

    REAL_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "BANCO REAL WIRE TRANSFER", 305),

    BANRISUL_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "BANRISUL WIRE TRANSFER", 306),

    HSBC_ONLINE_TRANSFER(PaymentMethodType.ONLINE_TRANSFER, "HSBC WIRE TRANSFER", 307),

    PS_BALANCE(PaymentMethodType.BALANCE, "PAGSEGURO BALANCE", 401),

    OI_PAGGO(PaymentMethodType.OI_PAGGO, "OI PAGGO - IT'S A KIND OF MOBILE PAYMENT", 501),

    BANCO_BRASIL_DIRECT_DEPOSIT(PaymentMethodType.DIRECT_DEPOSIT, "BANCO DO BRASIL DIRECT DEPOSIT", 701),

    HSBC_DIRECT_DEPOSIT(PaymentMethodType.DIRECT_DEPOSIT, "HSBC DIRECT DEPOSIT", 702),

    UNKNOWN_CODE(PaymentMethodType.UNKNOWN_TYPE, "UNKNOWN CODE. SEE ONLINE DOCUMENTATION", -1);

    private PaymentMethodType type;

    private String description;

    private Integer value;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param description
     *            - the description of the enum constant
     * @param value
     *            - the value of the enum constant
     */
    PaymentMethodCode(PaymentMethodType type, String description, Integer value) {
        this.type = type;
        this.description = description;
        this.value = value;
    }

    /**
     * Returns the enum constant of this type with the specified value. If a given value are not recognized return a
     * generic enum constant <code>UNKNOWN_CODE</code>
     * 
     * @param value
     *            - the value of the enum constant to be returned
     * @return the enum constant from a given value
     */
    public static PaymentMethodCode fromValue(Integer value) {

        for (PaymentMethodCode paymentMethodCode : values()) {
            if (paymentMethodCode.value.equals(value)) {
                return paymentMethodCode;
            }
        }

        UNKNOWN_CODE.setValue(value);
        return UNKNOWN_CODE;

    }

    /**
     * @return the type
     */
    public PaymentMethodType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(PaymentMethodType type) {
        this.type = type;
    }

    /**
     * @return this enum constant description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     *            - new description for this enum constant
     */
    public void setDescription(String type) {
        this.description = type;
    }

    /**
     * @return this enum constant value
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * @param value
     *            - new value for this enum constant
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}
