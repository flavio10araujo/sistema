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

import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.helper.PagSeguroUtil;

/**
 * Class that represents a Sender Document
 * 
 */
public class SenderDocument {

    /** Sender document type */
    private DocumentType type;

    /** Sender document value */
    private String value;

    /**
     * The constructor
     */
    public SenderDocument() {

    }

    /**
     * The constructor
     * 
     * @param documentType
     * @param value
     */
    public SenderDocument(DocumentType type, String value) {
        this.type = type;
        this.value = PagSeguroUtil.getOnlyNumbers(value);
    }

    /**
     * Gets document type
     * 
     * @return String
     */
    public DocumentType getType() {
        return this.type;
    }

    /**
     * Sets document type
     * 
     * @param String
     *            type
     */
    public void setType(DocumentType type) {
        this.type = type;
    }

    /**
     * Gets document value
     * 
     * @return Long
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets document value
     * 
     * @param String
     *            value
     */
    public void setValue(String value) {
        this.value = PagSeguroUtil.getOnlyNumbers(value);
    }

    /**
     * Gets toString class return string
     */
    @Override
    public String toString() {
        return "PagSeguroSenderDocument [type=" + type + ", value=" + value + "]";
    }
}
