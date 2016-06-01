package br.com.uol.pagseguro.enums;

/**
 * Enum constants that represents the countries accepted by PagSeguro on payment requests
 */
public enum Country {

    /** ISO 3166-1 Alfa-3 */
    BRA("BRAZIL");

    private String name;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param name
     *            - the country name
     */
    Country(String name) {
        this.name = name;
    }

    /**
     * @return this enum constant country name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            - the country name for this enum constant
     */
    public void setName(String name) {
        this.name = name;
    }

}
