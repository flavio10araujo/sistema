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
 * Enum constants that represents the metaDataItemKeys accepted by PagSeguro on payment requests
 */
public enum MetaDataItemKey {

    PASSENGER_CPF("PASSENGER CPF NUMBER"),

    PASSENGER_PASSPORT("PASSENGER PASSPORT NUMBER"),

    ORIGIN_CITY("ORIGIN CITY"),

    DESTINATION_CITY("DESTINATION CITY"),

    ORIGIN_AIRPORT_CODE("ORIGIN AIRPORT CODE"),

    DESTINATION_AIRPORT_CODE("DESTINATION AIRPORT CODE"),

    GAME_NAME("GAME NAME"),

    PLAYER_ID("PLAYER ID"),

    TIME_IN_GAME_DAYS("TIME IN GAME - MUST BE GIVEN IN DAYS"),

    MOBILE_NUMBER("MOBILE NUMBER THAT WILL RECEIVE THE RECHARGE"),

    PASSENGER_NAME("PASSENGER NAME");

    private String description;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param value
     *            - the value of the enum constant
     */
    MetaDataItemKey(String description) {
        this.description = description;
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
}
