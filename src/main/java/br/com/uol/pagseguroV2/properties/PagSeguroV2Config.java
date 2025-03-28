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
package br.com.uol.pagseguroV2.properties;

import br.com.uol.pagseguroV2.domain.AccountCredentials;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Encapsulates PagSeguro configuration for API calls
 */
@Component
public class PagSeguroV2Config {

    private static final String PRODUCTION_ENVIRONMENT = "production";
    private static final String SANDBOX_ENVIRONMENT = "sandbox";

    private static String pagSeguroEnvironment;
    private static String pagSeguroCredentialEmail;
    private static String pagSeguroCredentialProductionToken;
    private static String pagSeguroCredentialSandboxToken;
    private static String pagSeguroApplicationCharset;
    private static String pagSeguroLogActive;

    private PagSeguroV2Config() {
    }

    @Value("${pagSeguro.environment.type}")
    public void setPagSeguroEnvironment(String pagSeguroEnvironment) {
        PagSeguroV2Config.pagSeguroEnvironment = pagSeguroEnvironment;
    }

    @Value("${pagSeguro.credential.email}")
    public void setPagSeguroCredentialEmail(String pagSeguroCredentialEmail) {
        PagSeguroV2Config.pagSeguroCredentialEmail = pagSeguroCredentialEmail;
    }

    @Value("${pagSeguro.credential.production.token}")
    public void setPagSeguroCredentialProductionToken(String pagSeguroCredentialProductionToken) {
        PagSeguroV2Config.pagSeguroCredentialProductionToken = pagSeguroCredentialProductionToken;
    }

    @Value("${pagSeguro.credential.sandbox.token}")
    public void setPagSeguroCredentialSandboxToken(String pagSeguroCredentialSandboxToken) {
        PagSeguroV2Config.pagSeguroCredentialSandboxToken = pagSeguroCredentialSandboxToken;
    }

    @Value("${pagSeguro.application.charset}")
    public void setPagSeguroApplicationCharset(String pagSeguroApplicationCharset) {
        PagSeguroV2Config.pagSeguroApplicationCharset = pagSeguroApplicationCharset;
    }

    @Value("${pagSeguro.log.active}")
    public void setPagSeguroLogActive(String pagSeguroLogActive) {
        PagSeguroV2Config.pagSeguroLogActive = pagSeguroLogActive;
    }

    public static AccountCredentials getAccountCredentials() throws PagSeguroServiceException {
        String email = pagSeguroCredentialEmail;
        String productionToken = pagSeguroCredentialProductionToken;
        String sandboxToken = pagSeguroCredentialSandboxToken;

        email = email == null ? null : email.trim();
        productionToken = productionToken == null ? null : productionToken.trim();
        sandboxToken = sandboxToken == null ? null : sandboxToken.trim();

        // it is validated at this point to put a error message in the exception
        if (email == null || "".equals(email) || productionToken == null || "".equals(productionToken)
                || sandboxToken == null || "".equals(sandboxToken)) {

            throw new PagSeguroServiceException(
                    "To use credentials from config.properties file you must "
                            + "configure the properties credential.email, credential.production.token and credential.sandbox.token. Currently "
                            + "credential.email=[" + email + "], credential.production.token=[" + productionToken
                            + "] and credential.sandbox.token=[" + sandboxToken + "].");

        }

        return new AccountCredentials(email, productionToken, sandboxToken);
    }

    public static String getEnvironment() {
        return pagSeguroEnvironment;
    }

    public static String getApplicationCharset() {
        return pagSeguroApplicationCharset;
    }

    public static boolean getLogActive() {
        return "true".equals(pagSeguroLogActive);
    }

    public static boolean isSandboxEnvironment() {
        return SANDBOX_ENVIRONMENT.equals(pagSeguroEnvironment);
    }
}
