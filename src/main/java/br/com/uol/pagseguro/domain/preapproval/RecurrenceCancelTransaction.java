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

/**
 * Represents a PagSeguro recurrence cancel transaction
 */
public class RecurrenceCancelTransaction {

    /** Recurrence code */
    private String code;

    /** Recurrence cancel date */
    private Date date;

    /**
     * Initializes a new instance of the RecurrenceCancelTransaction class
     */
    public RecurrenceCancelTransaction() {

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
     * @return the initialDate
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the initial date
     * 
     * @param initialDate
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder()//
                .append("RecurrenceCancelTransaction [")//
                .append("code=\"")//
                .append(code + "\"")//
                .append(",date=")//
                .append(date)//
                .append("]");
        return builder.toString();
    }
}
