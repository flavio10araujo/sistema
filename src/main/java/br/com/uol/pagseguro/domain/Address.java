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

package br.com.uol.pagseguro.domain;

/**
 * Represents an address location, typically for shipping or billing purposes
 * 
 * @see Shipping
 */
public class Address {

    /**
     * Zip/postal code
     */
    private String postalCode;

    /**
     * Street name
     */
    private String street;

    /**
     * Number
     */
    private String number;

    /**
     * Apartment, suite number or any other qualifier after the street/number pair. Example: Apt 274, building A.
     */
    private String complement;

    /**
     * District, county or neighborhood
     */
    private String district;

    /**
     * City
     */
    private String city;

    /**
     * State or province acronym
     */
    private String state;

    /**
     * Country code (ISO 3166-1 Alfa-3)
     */
    private String country;

    /**
     * Initializes a newly created instance of this type
     */
    public Address() {
    }

    /**
     * Initializes a newly created instance of this type with the specified arguments
     * 
     * @param country
     *            the country code (ISO 3166-1 Alfa-3), i.e BRA
     * @param state
     *            the state/province acronym, i.e. SP
     * @param city
     *            the city name, i.e. São Paulo
     * @param district
     *            the district, county or neighborhood name, i.e. Pinheiros
     * @param postalCode
     *            the zip/postal code, i.e. 01452002
     * @param street
     *            the street name, i.e. Brigadeiro Faria Lima
     * @param number
     *            the number, i.e. 1384
     * @param complement
     *            the complement details, i.e. 3o andar
     */
    public Address(String country, String state, String city, String district, String postalCode, String street,
            String number, String complement) {
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.district = district;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    /**
     * @return the street name
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * @return the complement details
     */
    public String getComplement() {
        return this.complement;
    }

    /**
     * @return the district, county or neighborhood name
     */
    public String getDistrict() {
        return this.district;
    }

    /**
     * @return the city name
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @return the state/province acronym
     */
    public String getState() {
        return this.state;
    }

    /**
     * @return the zip/postal code
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * @return the country code (ISO 3166-1 Alfa-3)
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * @param street
     *            the street name to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @param complement
     *            the complement details to set
     */
    public void setComplement(String complement) {
        this.complement = complement;
    }

    /**
     * @param district
     *            the district, county or neighborhood to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @param city
     *            the city name to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @param state
     *            the state/province acronym to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @param postalCode
     *            the zip/postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @param country
     *            the country code to set (ISO 3166-1 Alfa-3)
     */
    public void setCountry(String country) {
        this.country = country;
    }

}
