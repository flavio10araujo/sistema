/*
 ************************************************************************
 Copyright [2014] [PagSeguro Internet Ltda.]

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

package br.com.uol.pagseguro.domain.paymentrequest;

import br.com.uol.pagseguro.helper.PagSeguroUtil;

/**
 * Represents the party on the transaction that is sending the money
 */
public class PaymentRequestSender {

    /**
     * PaymentRequestSender e-mail
     */
    private String email;

    /**
     * PaymentRequestSender name
     */
    private String name;

    /**
     * Initializes a new instance of the PaymentRequestSender class
     */
    public PaymentRequestSender() {

    }

    /**
     * Initializes a new instance of the PaymentRequestSender class with the specified arguments
     * 
     * @param email
     * @param name
     */
    public PaymentRequestSender(String email, String name) {
        this.email = email;
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
     * @return string
     */
    @Override
    public String toString() {
        return "PaymentRequestSender [email=" + email + ", name=" + name + "]";
    }
}
