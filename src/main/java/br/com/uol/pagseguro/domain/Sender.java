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

import java.util.ArrayList;
import java.util.List;

import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.helper.PagSeguroUtil;

/**
 * Represents the party on the transaction that is sending the money
 */
public class Sender {

    /** Sender name */
    private String name;

    /** Sender e-mail */
    private String email;

    /** Sender born date */
    private String bornDate;

    /** Sender phone */
    private Phone phone;

    /** Sender documents */
    private List<SenderDocument> documents;

    /** Sender hash */
    private String hash;

    /** Sender ip */
    private String ip;

    /**
     * Initializes a new instance of the Sender class
     */
    public Sender() {

    }

    /**
     * Initializes a new instance of the Sender class
     * 
     * @param name
     * @param email
     */
    public Sender(String name, String email) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
        this.email = email;
    }

    /**
     * Initializes a new instance of the Sender class
     * 
     * @param name
     * @param email
     * @param phone
     */
    public Sender(String name, String email, Phone phone) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
        this.email = email;
        this.phone = phone;
    }

    /**
     * Initializes a new instance of the Sender class
     * 
     * @param name
     * @param email
     * @param phone
     * @param document
     */
    public Sender(String name, String email, Phone phone, SenderDocument document) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
        this.email = email;
        this.phone = phone;
        this.documents = new ArrayList<SenderDocument>();
        documents.add(document);
    }

    /**
     * Initializes a new instance of the Sender class
     * 
     * @param name
     * @param email
     * @param phone
     * @param document
     * @param bornDate
     */
    public Sender(String name, String email, Phone phone, SenderDocument document, String bornDate) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
        this.email = email;
        this.phone = phone;
        this.documents = new ArrayList<SenderDocument>();
        documents.add(document);
        this.bornDate = bornDate;
    }

    /**
     * Initializes a new instance of the Sender class
     * 
     * @param name
     * @param email
     * @param phone
     * @param bornDate
     */
    public Sender(String name, String email, Phone phone, String bornDate) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
        this.email = email;
        this.phone = phone;
        this.bornDate = bornDate;
    }

    /**
     * @return the sender name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the sender name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = PagSeguroUtil.removeExtraSpaces(name);
    }

    /**
     * @return the sender e-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the sender e-mail
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the sender born date
     */
    public String getBornDate() {
        return bornDate;
    }

    /**
     * Sets the sender born date
     * 
     * @param email
     */
    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    /**
     * @return the sender phone
     */
    public Phone getPhone() {
        if (phone == null) {
            phone = new Phone();
        }
        return phone;
    }

    /**
     * Sets the sender phone
     * 
     * @param phone
     */
    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    /**
     * Gets sender documents list
     * 
     * @return the sender documents list
     */
    public List<SenderDocument> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<SenderDocument>();
        }
        return documents;
    }

    /**
     * Sets the sender documents list
     * 
     * @param documents
     */
    public void setDocuments(List<SenderDocument> documents) {
        this.documents = documents;
    }

    /**
     * Add a document for sender documents list
     * 
     * @param document
     */
    public void addDocument(SenderDocument document) {
        getDocuments().add(document);
    }

    /**
     * Add a document for sender documents list
     * 
     * @param type
     * @param value
     */
    public void addDocument(DocumentType type, String value) {
        addDocument(new SenderDocument(type, value));
    }

    /**
     * @return the sender hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the sender hash
     * 
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return the sender ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the sender ip
     * 
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * return string
     */
    @Override
    public String toString() {
        return "Sender [name=" + name + ", email=" + email + ", phone=" + phone + ", documents=" + documents
                + ", bornDate=" + bornDate + "]";
    }

}
