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

package br.com.uol.pagseguro.domain.preapproval;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.enums.RecurrencePeriod;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.preapproval.RecurrenceService;

/**
 * 
 * Represents a recurrence transaction
 *
 */
public class Recurrence {

    /**
     * The initial date that the recurrence will be sent
     */
    private String initialDate;

    /**
     * Payment request that will be sent by the recurrence
     */
    private PaymentRequest paymentRequest;

    /**
     * Number of payment requests that will be sent
     */
    private Integer paymentRequestsQuantity;

    /**
     * Period in which the recurrence will live
     */
    private RecurrencePeriod period;

    /**
     * Initializes a new instance of the Recurrence class
     */
    public Recurrence() {

    }

    /**
     * Initializes a new instance of the Recurrence class with the specified arguments
     * 
     * @param initialDate
     * @param paymentRequest
     * @param paymentRequestsQuantity
     * @param period
     */
    public Recurrence(String initialDate, PaymentRequest paymentRequest, Integer paymentRequestsQuantity,
            RecurrencePeriod period) {
        super();
        this.initialDate = initialDate;
        this.paymentRequest = paymentRequest;
        this.paymentRequestsQuantity = paymentRequestsQuantity;
        this.period = period;
    }

    /**
     * @return the initialDate
     */
    public String getInitialDate() {
        return initialDate;
    }

    /**
     * Sets the initialDate
     * 
     * @param initialDate
     */
    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * @return the paymentRequest
     */
    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    /**
     * Sets the paymentRequest
     * 
     * @param paymentRequest
     */
    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    /**
     * @return the paymentRequestsQuantity
     */
    public Integer getPaymentRequestsQuantity() {
        return paymentRequestsQuantity;
    }

    /**
     * Sets the paymentRequestsQuantity
     * 
     * @param paymentRequestsQuantity
     */
    public void setPaymentRequestsQuantity(Integer paymentRequestsQuantity) {
        this.paymentRequestsQuantity = paymentRequestsQuantity;
    }

    /**
     * @return the period
     */
    public RecurrencePeriod getPeriod() {
        return period;
    }

    /**
     * Sets the period
     * 
     * @param period
     */
    public void setPeriod(RecurrencePeriod period) {
        this.period = period;
    }

    /**
     * Calls the PagSeguro web service and register this recurrence request
     * 
     * @param credentials
     * @return The payment request code
     * @throws PagSeguroServiceException
     */
    public String register(Credentials credentials) throws PagSeguroServiceException {
        return RecurrenceService.createRecurrence(credentials, this);
    }

    /**
     * Calls the PagSeguro web service and return a payment request
     * 
     * @param credentials
     * @param recurrenceCode
     * @return The recurrence
     * @throws PagSeguroServiceException
     */
    public RecurrenceTransaction search(Credentials credentials, String recurrenceCode)
            throws PagSeguroServiceException {
        return RecurrenceService.findByCode(credentials, recurrenceCode);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder()//
                .append("Recurrence [")//
                .append("initialDate=\"")//
                .append(initialDate + "\"")//
                .append(",paymentRequest=")//
                .append(paymentRequest)//
                .append(",paymentRequestsQuantity=")//
                .append(paymentRequestsQuantity)//
                .append(",period=")//
                .append(period)//
                .append("]");
        return builder.toString();
    }
}
