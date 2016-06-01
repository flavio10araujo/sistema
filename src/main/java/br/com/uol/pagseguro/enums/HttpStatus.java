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

package br.com.uol.pagseguro.enums;

/**
 * Enum constants that represents the HTTP Status that can be returned on payment requests
 */
public enum HttpStatus {

    OK(200, "STANDARD RESPONSE FOR SUCCESSFUL HTTP REQUESTS"),

    BAD_REQUEST(400, "THE REQUEST CANNOT BE FULFILLED DUE TO BAD SYNTAX"),

    UNAUTHORIZED(401, "THE AUTHENTICATION IS POSSIBLE BUT HAS FAILED"),

    FORBIDDEN(403, "THE SERVER REFUSES TO RESPOND TO REQUEST"),

    NOT_FOUND(404, "THE REQUESTED RESOURCE COULD NOT BE FOUND"),

    INTERNAL_SERVER_ERROR(500, "GENERIC ERROR MESSAGE"),

    BAD_GATEWAY(502, "SERVER RECEIVED AN INVALID RESPONDE FROM UPSTREAM SERVER"),

    SERVICE_UNAVAILABLE(503, "THE SERVER IS CURRENTLY UNAVAILABLE"),

    UNKNOWN_STATUS(-1, "UNKNOWN STATUS. SEE ONLINE DOCUMENTATION");

    private Integer code;

    private String description;

    /**
     * Initializes a newly created enum constant of this type with the specified arguments
     * 
     * @param code
     *            - the http status code
     * @param description
     *            - the http status description
     */
    HttpStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Returns the enum constant of this type with the specified value. If a given value are not recognized return a
     * generic enum constant <code>UNKNOWN_STATUS</code>
     * 
     * @param code
     *            - the http code of the enum constant to be returned
     * @return the enum constant from a given value
     */
    public static HttpStatus fromCode(Integer code) {

        for (HttpStatus httpStatus : values()) {
            if (httpStatus.code.equals(code)) {
                return httpStatus;
            }
        }

        UNKNOWN_STATUS.setCode(code);
        return UNKNOWN_STATUS;

    }

    /**
     * @return this enum constant http code
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * @param code
     *            - new http code for this enum constant
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return this enum constant http description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     *            - new http description for this enum constant
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
