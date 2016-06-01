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
package br.com.uol.pagseguro.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.enums.HttpStatus;

/**
 * Encapsulates a problem that occurred calling a PagSeguro web service
 */
public class PagSeguroServiceException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3042057812162033491L;

    /**
     * @var HttpStatus see HttpStatus
     */
    private HttpStatus httpStatus;

    /**
     * @var list
     */
    private List<Error> errors;

    /**
     * Construct
     * 
     * @param httpStatus
     */
    public PagSeguroServiceException(String message) {
        super(message);
    }

    /**
     * Construct
     * 
     * @param httpStatus
     */
    public PagSeguroServiceException(String message, Exception exception) {
        super(message, exception);
    }

    /**
     * Construct
     * 
     * @param httpStatus
     */
    public PagSeguroServiceException(HttpStatus httpStatus, Exception exception) {
        super(String.format("HTTP %1$s - %2$s [%3$s] \n %4$s", httpStatus.getCode(), httpStatus,
                httpStatus.getDescription(), exception.getMessage()));
        this.httpStatus = httpStatus;
    }

    /**
     * Construct
     * 
     * @param httpStatus
     */
    public PagSeguroServiceException(HttpStatus httpStatus) {
        super(String.format("HTTP %1$s - %2$s [%3$s]", httpStatus.getCode(), httpStatus, httpStatus.getDescription()));
        this.httpStatus = httpStatus;
    }

    /**
     * Construct
     * 
     * @param httpStatus
     * @param erros
     */
    public PagSeguroServiceException(HttpStatus httpStatus, List<Error> erros) {
        super(String.format("HTTP %1$s - %2$s [%3$s]", httpStatus.getCode(), httpStatus, httpStatus.getDescription()));
        this.httpStatus = httpStatus;
        this.errors = erros;
    }

    /**
     * @return the httpStatus
     */
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    /**
     * @param httpStatus
     *            the httpStatus to set
     */
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     * @return the errors
     */
    public List<Error> getErrors() {
        if (this.errors == null) {
            this.errors = new ArrayList<Error>();
        }
        return this.errors;
    }

    /**
     * @param errors
     *            the errors to set
     */
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {

        StringBuilder message = new StringBuilder(super.getMessage());

        if (!this.getErrors().isEmpty()) {

            for (Iterator<Error> iter = this.errors.iterator(); iter.hasNext();) {

                Error error = (Error) iter.next();

                message.append("\n").append(error.getCode()).append(" - ").append(error.getMessage());

            }
        }

        return message.toString();
    }

}
