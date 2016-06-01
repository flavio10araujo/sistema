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
 * Enum constants that represents a list of known shippingTypes
 */
public enum ShippingType {

    PAC("ECONOMIC SHIPMENT", 1),

    SEDEX("EXPRESS SHIPMENT OF DOCUMENTS AND GOODS", 2),

    NOT_SPECIFIED("NOT SPECIFIED SHIPMENT", 3);

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
    ShippingType(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    /**
     * Returns the enum constant of this type with the specified value.
     * 
     * @param value
     *            - the value of the enum constant to be returned
     * @return the enum constant from a given value
     */
    public static ShippingType fromValue(Integer value) {

        for (ShippingType shippingType : values()) {
            if (shippingType.value.equals(value)) {
                return shippingType;
            }
        }

        return null;

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
    public void setDescription(String description) {
        this.description = description;
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
