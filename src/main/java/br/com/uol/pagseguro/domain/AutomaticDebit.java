/*
 * ***********************************************************************
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
 * ***********************************************************************
 */

package br.com.uol.pagseguro.domain;

import java.util.Date;

/**
 * Contains Automatic debit information
 */
public class AutomaticDebit {

    /**
     * Bank name
     */
    private String bank;

    /**
     * Date in which the automatic debit was scheduled
     */
    private Date scheduledDate;

    /**
     * Date in which the automatic debit was made
     */
    private Date debitDate;

    /**
     * Initializes a new instance of the AutomaticDebit class with the specified arguments
     *
     * @param bank
     * @param scheduledDate
     * @param debitDate
     */
    public AutomaticDebit(final String bank, final Date scheduledDate, final Date debitDate) {

        this.bank = bank;
        this.scheduledDate = scheduledDate;
        this.debitDate = debitDate;
    }

    /**
     * Initializes a new instance of the AutomaticDebit class
     */
    public AutomaticDebit() {

    }

    /**
     * @return bank
     */
    public String getBank() {
        return bank;
    }

    /**
     * Sets the bank name
     *
     * @param bank
     */
    public void setBank(final String bank) {
        this.bank = bank;
    }

    /**
     * @return scheduleDate
     */
    public Date getScheduledDate() {
        return scheduledDate;
    }

    /**
     * Sets the scheduled date
     * @param scheduledDate
     */
    public void setScheduledDate(final Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    /**
     * @return debitDate
     */
    public Date getDebitDate() {
        return debitDate;
    }

    /**
     * Sets the debit date
     *
     * @param debitDate
     */
    public void setDebitDate(final Date debitDate) {
        this.debitDate = debitDate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()//
                .append("AutomaticDebit [")//
                .append("bank=\"")//
                .append(bank + "\"")//
                .append(",scheduleDate=")//
                .append(scheduledDate)//
                .append(",debitDate=")//
                .append(debitDate)//
                .append("]");
        return builder.toString();
    }
}
