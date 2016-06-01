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

package br.com.uol.pagseguro.domain;

/**
 * Represents the receiver of a transaction
 */
public class Receiver {

    /**
     * Receiver e-mail
     */
    private String email;

    /**
     * Receiver name
     */
    private String name;

    /**
     * Receiver phone
     */
    private Phone phone;

    public Receiver() {
        this.phone = new Phone();
    }

    /**
     * Initializes a new instance of the PaymentRequest class with the specified arguments
     *
     * @param email
     */
    public Receiver(final String email) {

        this.email = email;
    }

    /**
     * Initializes a new instance of the PaymentRequest class with the specified arguments
     *
     * @param email
     * @param name
     * @param phone
     */
    public Receiver(final String email, final String name, final Phone phone) {

        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    /**
     * @return email
     */
    public String getEmail() {

        return email;
    }

    /**
     * Sets the email
     *
     * @param email
     */
    public void setEmail(final String email) {

        this.email = email;
    }

    /**
     * @return name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the name
     *
     * @param name
     */
    public void setName(final String name) {

        this.name = name;
    }

    /**
     * @return phone
     */
    public Phone getPhone() {

        return phone;
    }

    /**
     * Sets the phone
     *
     * @param phone
     */
    public void setPhone(final Phone phone) {

        this.phone = phone;
    }
}
