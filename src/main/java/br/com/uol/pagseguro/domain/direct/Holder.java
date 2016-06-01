package br.com.uol.pagseguro.domain.direct;

import br.com.uol.pagseguro.domain.Document;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.helper.PagSeguroUtil;

/**
 * Represents the holder of the credit card payment
 */
public class Holder {

    /**
     * Holder name
     */
    private final String name;

    /**
     * Holder phone
     */
    private final Phone phone;

    /**
     * Holder document
     */
    private final Document document;

    /**
     * Holder birth date
     */
    private final String birthDate;

    /**
     * Initializes a new instance of the Holder class
     * 
     * @param name
     * @param phone
     * @param document
     * @param birthDate
     */
    public Holder(String name, Phone phone, Document document, String birthDate) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
        this.phone = phone;
        this.document = document;
        this.birthDate = birthDate;
    }

    /**
     * @return the holder name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the holder phone
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * @return the holder document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @return the holder birth date
     */
    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Holder[");
        sb.append("name=" + name);
        sb.append(",phone=" + phone);
        sb.append(",document=" + document);
        sb.append(",birthDate=" + birthDate);
        sb.append("]");
        return sb.toString();
    }

}
