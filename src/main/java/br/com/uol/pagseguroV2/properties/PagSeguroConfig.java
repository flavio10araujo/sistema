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
import br.com.uol.pagseguroV2.domain.ApplicationCredentials;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Encapsulates PagSeguro configuration for API calls
 */
@Component
public class PagSeguroConfig {

    private static final String PRODUCTION_ENVIRONMENT = "production";
    private static final String SANDBOX_ENVIRONMENT = "sandbox";

    private static String pagSeguroEnvironment;
    private static String pagSeguroCredentialEmail;
    private static String pagSeguroCredentialProductionToken;
    private static String pagSeguroCredentialSandboxToken;
    private static String pagSeguroCredentialProductionAppId;
    private static String pagSeguroCredentialProductionAppKey;
    private static String pagSeguroCredentialSandboxAppId;
    private static String pagSeguroCredentialSandboxAppKey;
    private static String pagSeguroApplicationCharset;
    private static String pagSeguroLogActive;

    /**
     * Module Version
     *
     * @var string
     */
    private static String moduleVersion;
    /**
     * Cms Version
     *
     * @var String
     */
    private static String cmsVersion;

    private PagSeguroConfig() {
    }

    @Value("${pagSeguro.environment.type}")
    public void setPagSeguroEnvironment(String pagSeguroEnvironment) {
        PagSeguroConfig.pagSeguroEnvironment = pagSeguroEnvironment;
    }

    @Value("${pagSeguro.credential.email}")
    public void setPagSeguroCredentialEmail(String pagSeguroCredentialEmail) {
        PagSeguroConfig.pagSeguroCredentialEmail = pagSeguroCredentialEmail;
    }

    @Value("${pagSeguro.credential.production.token}")
    public void setPagSeguroCredentialProductionToken(String pagSeguroCredentialProductionToken) {
        PagSeguroConfig.pagSeguroCredentialProductionToken = pagSeguroCredentialProductionToken;
    }

    @Value("${pagSeguro.credential.sandbox.token}")
    public void setPagSeguroCredentialSandboxToken(String pagSeguroCredentialSandboxToken) {
        PagSeguroConfig.pagSeguroCredentialSandboxToken = pagSeguroCredentialSandboxToken;
    }

    @Value("${pagSeguro.credential.production.appId}")
    public void setPagSeguroCredentialProductionAppId(String pagSeguroCredentialProductionAppId) {
        PagSeguroConfig.pagSeguroCredentialProductionAppId = pagSeguroCredentialProductionAppId;
    }

    @Value("${pagSeguro.credential.production.appKey}")
    public void setPagSeguroCredentialProductionAppKey(String pagSeguroCredentialProductionAppKey) {
        PagSeguroConfig.pagSeguroCredentialProductionAppKey = pagSeguroCredentialProductionAppKey;
    }

    @Value("${pagSeguro.credential.sandbox.appId}")
    public void setPagSeguroCredentialSandboxAppId(String pagSeguroCredentialSandboxAppId) {
        PagSeguroConfig.pagSeguroCredentialSandboxAppId = pagSeguroCredentialSandboxAppId;
    }

    @Value("${pagSeguro.credential.sandbox.appKey}")
    public void setPagSeguroCredentialSandboxAppKey(String pagSeguroCredentialSandboxAppKey) {
        PagSeguroConfig.pagSeguroCredentialSandboxAppKey = pagSeguroCredentialSandboxAppKey;
    }

    @Value("${pagSeguro.application.charset}")
    public void setPagSeguroApplicationCharset(String pagSeguroApplicationCharset) {
        PagSeguroConfig.pagSeguroApplicationCharset = pagSeguroApplicationCharset;
    }

    @Value("${pagSeguro.log.active}")
    public void setPagSeguroLogActive(String pagSeguroLogActive) {
        PagSeguroConfig.pagSeguroLogActive = pagSeguroLogActive;
    }

    /**
     * To activate the PagSeguro logging tool, set the <b>log.path<b> property in <b>pagseguro-config.properties</b> file.
     *
     * @return the path to PagSeguro log file
     */
    public static String getLogPath() {
        //return resourceBundle.getString("log.path");
        return "";
    }

    public static String getLoggerImplementation() {
        //return resourceBundle.getString("logger.implementation");
        return "";
    }

    /**
     * Account credentials read from config file <b>pagseguro-config.properties</b> To read the account credentials from
     * config, you have to set <b>credential.email</b>, <b>credential.production.token</b> and
     * <b>credential.sandbox.token</b> in the <b>pagseguro-config.properties</b> file
     *
     * @return the account credentials read from <b>pagseguro-config.properties</b> file.
     * @throws Exception
     */
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

    /**
     * Application credentials read from config file <b>pagseguro-config.properties</b> To read the application credentials from
     * config, you have to set <b>credential.production.appId</b>, <b>credential.production.appKey</b>, <b>credential.sandbox.appId</b>
     * and <b>credential.sandbox.appKey</b> in the <b>pagseguro-config.properties</b> file
     *
     * @return the application credentials read from <b>pagseguro-config.properties</b> file.
     * @throws Exception
     */
    public static ApplicationCredentials getApplicationCredentials() throws PagSeguroServiceException {
        String productionAppId = pagSeguroCredentialProductionAppId;
        String productionAppKey = pagSeguroCredentialProductionAppKey;
        String sandboxAppId = pagSeguroCredentialSandboxAppId;
        String sandboxAppKey = pagSeguroCredentialSandboxAppKey;

        productionAppId = productionAppId == null ? null : productionAppId.trim();
        productionAppKey = productionAppKey == null ? null : productionAppKey.trim();
        sandboxAppId = sandboxAppId == null ? null : sandboxAppId.trim();
        sandboxAppKey = sandboxAppKey == null ? null : sandboxAppKey.trim();

        // it is validated at this point to put a error message in the exception
        if (productionAppId == null || "".equals(productionAppId) || productionAppKey == null || "".equals(productionAppKey)
                || sandboxAppId == null || "".equals(sandboxAppId) || sandboxAppKey == null || "".equals(sandboxAppKey)) {

            throw new PagSeguroServiceException(
                    "To use application credentials from config.properties file you must "
                            + "configure the properties credential.production.appId, credential.production.appKey, credential.sandbox.appId and credential.sandbox.appKey. Currently "
                            + "credential.production.appId=[" + productionAppId + "], credential.production.appKey=[" + productionAppKey
                            + "],credential.sandbox.appId=[" + sandboxAppId + "]  and credential.sandbox.appKey=[" + sandboxAppKey + "].");

        }

        return new ApplicationCredentials(productionAppId, productionAppKey, sandboxAppId, sandboxAppKey);
    }

    /**
     * Get environment
     *
     * @return string
     */
    public static String getEnvironment() {
        return pagSeguroEnvironment;
    }

    /**
     * Get Charset UTF-8, ISO-8859-1
     *
     * @return string
     */
    public static String getApplicationCharset() {
        return pagSeguroApplicationCharset;
    }

    /**
     * Get module version
     *
     * @return string
     */
    public static String getModuleVersion() {
        return moduleVersion;
    }

    /**
     * Set module version
     *
     * @param moduleVersion
     */
    public static void setModuleVersion(String moduleVersion) {
        PagSeguroConfig.moduleVersion = moduleVersion;
    }

    /**
     * Get Cms Version
     *
     * @return string
     */
    public static String getCmsVersion() {
        return cmsVersion;
    }

    /**
     * @return boolean
     */
    public static boolean getLogActive() {
        return "true".equals(pagSeguroLogActive);
    }

    public static void setSandboxEnvironment() {
        //instance.environment = SANDBOX_ENVIRONMENT;
    }

    public static void setProductionEnvironment() {
        //instance.environment = PRODUCTION_ENVIRONMENT;
    }

    public static boolean isSandboxEnvironment() {
        return SANDBOX_ENVIRONMENT.equals(pagSeguroEnvironment);
    }
}
