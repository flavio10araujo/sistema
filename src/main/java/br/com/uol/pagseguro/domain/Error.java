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

import br.com.uol.pagseguro.exception.PagSeguroServiceException;

/**
 * Represents a PagSeguro web service error
 * 
 * @see PagSeguroServiceException
 */
public class Error {

    /**
     * Error code
     */
    private String code;

    /**
     * Error description
     */
    private String message;

    /**
     * Initializes a newly created instance of this type
     */
    public Error() {
    }

    /**
     * Initializes a newly created instance of this type with the specified arguments
     * 
     * @param code
     *            the ws error code, i.e. 13001
     * @param message
     *            the ws error description, i.e. invalid notification code value: {0}
     */
    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return the ws error code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code
     *            the ws error code to set, i.e. 13001
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the ws error description
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param message
     *            the ws error description to set, i.e. invalid notification code value: {0}
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
