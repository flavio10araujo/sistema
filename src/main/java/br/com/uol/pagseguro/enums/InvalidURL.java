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
 * Enum constants that represents local URLs not accepted on payment requests
 * 
 */
public enum InvalidURL {

    LOCALHOST("localhost"),

    IP_V4("127.0.0.1"),

    IP_V6("::1");

    private String value;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param value
     *            - the value of the enum constant
     */
    InvalidURL(String value) {
        this.value = value;
    }

    /**
     * 
     * @return an array of enum constants
     */
    public InvalidURL[] getEnumInvalidURL() {
        return InvalidURL.values();
    }

    /**
     * @return this enum constant value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     *            - new value for this enum constant
     */
    public void setValue(String value) {
        this.value = value;
    }

}
