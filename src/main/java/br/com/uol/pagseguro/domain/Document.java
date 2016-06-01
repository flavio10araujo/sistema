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

package br.com.uol.pagseguro.domain;

import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.helper.PagSeguroUtil;

/**
 * Represents a taxpayer identification document, issued to Brazilians and foreigners
 */
public class Document {

    /**
     * Document type
     */
    private DocumentType type;

    /**
     * Document value
     */
    private String value;

    /**
     * Initializes a newly created instance of this type
     */
    public Document() {
    }

    /**
     * Initializes a newly created instance of this type with the specified arguments
     * 
     * @param type
     *            the document type
     * @param value
     *            the document value
     */
    public Document(DocumentType type, String value) {
        this.type = type;
        this.value = PagSeguroUtil.getOnlyNumbers(value);
    }

    /**
     * @return the document type
     */
    public DocumentType getType() {
        return this.type;
    }

    /**
     * @param type
     *            the document type to set
     */
    public void setType(DocumentType type) {
        this.type = type;
    }

    /**
     * @return the document value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     *            the document value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
