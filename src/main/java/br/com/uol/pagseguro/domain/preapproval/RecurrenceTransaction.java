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

import java.util.Date;

import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.enums.RecurrencePeriod;
import br.com.uol.pagseguro.enums.RecurrenceStatus;

/**
 * Represents a PagSeguro recurrence transaction
 */
public class RecurrenceTransaction {

    /** Recurrence code */
    private String code;

    /** Recurrence status */
    private RecurrenceStatus status;

    /** Recurrence period */
    private RecurrencePeriod period;

    /** Payment requests quantity */
    private Integer paymentRequestsQuantity;

    /** Recurrence initial date */
    private Date initialDate;

    /** Transaction payment request */
    private PaymentRequest paymentRequest;

    /**
     * Initializes a new instance of the RecurrenceTransaction class
     */
    public RecurrenceTransaction() {

        this.paymentRequest = new PaymentRequest();
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code
     * 
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the status
     */
    public RecurrenceStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     * 
     * @param status
     */
    public void setStatus(RecurrenceStatus status) {
        this.status = status;
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
     * @return the paymentRequestsQuantity
     */
    public Integer getPaymentRequestsQuantity() {
        return paymentRequestsQuantity;
    }

    /**
     * Sets the payment requests quantity
     * 
     * @param paymentRequestsQuantity
     */
    public void setPaymentRequestsQuantity(Integer paymentRequestsQuantity) {
        this.paymentRequestsQuantity = paymentRequestsQuantity;
    }

    /**
     * @return the initialDate
     */
    public Date getInitialDate() {
        return initialDate;
    }

    /**
     * Sets the initial date
     * 
     * @param initialDate
     */
    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * @return the paymentRequest
     */
    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    /**
     * Sets the payment request
     * 
     * @param paymentRequest
     */
    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "RecurrenceTransaction [code=" + code + ", status=" + status + ", period=" + period
                + ", paymentRequestQuantity=" + paymentRequestsQuantity + ", initialDate=" + initialDate
                + ", paymentRequest=" + paymentRequest + "]";
    }
}
