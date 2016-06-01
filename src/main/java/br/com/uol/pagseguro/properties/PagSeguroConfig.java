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
package br.com.uol.pagseguro.properties;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.ApplicationCredentials;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;

/**
 * Encapsulates PagSeguro configuration for API calls
 */
public class PagSeguroConfig {

    private static final String PRODUCTION_ENVIRONMENT = "production";

    private static final String SANDBOX_ENVIRONMENT = "sandbox";

    private String environment;

    private static PagSeguroConfig instance;

    private PagSeguroConfig() {
    }

    /**
     * @see ResourceBundle
     */
    private static ResourceBundle resourceBundle;

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

    static {
        resourceBundle = ResourceBundle.getBundle("pagseguro-config", Locale.getDefault());
        if (instance == null)
            instance = new PagSeguroConfig();
        instance.environment = resourceBundle.getString("environment");
    }

    /**
     * To activate the PagSeguro logging tool, set the <b>log.path<b> property in <b>pagseguro-config.properties</b>
     * file.
     * 
     * @return the path to PagSeguro log file
     */
    public static String getLogPath() {
        return resourceBundle.getString("log.path");
    }

    public static String getLoggerImplementation() {
        return resourceBundle.getString("logger.implementation");
    }

    /**
     * Account credentials read from config file <b>pagseguro-config.properties</b> To read the account credentials from
     * config, you have to set <b>credential.email</b>, <b>credential.production.token</b> and
     * <b>credential.sandbox.token</b> in the <b>pagseguro-config.properties</b> file
     * 
     * @return the account credentials read from <b>pagseguro-config.properties</b> file.
     * @throws Exception
     * 
     */
    public static AccountCredentials getAccountCredentials() throws PagSeguroServiceException {

        String email = resourceBundle.getString("credential.email");
        String productionToken = resourceBundle.getString("credential.production.token");
        String sandboxToken = resourceBundle.getString("credential.sandbox.token");

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
     * 
     */
    public static ApplicationCredentials getApplicationCredentials() throws PagSeguroServiceException {

        String productionAppId = resourceBundle.getString("credential.production.appId");
        String productionAppKey = resourceBundle.getString("credential.production.appKey");
        String sandboxAppId = resourceBundle.getString("credential.sandbox.appId");
        String sandboxAppKey = resourceBundle.getString("credential.sandbox.appKey");

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
        return instance.environment;
    }

    /**
     * Get Charset UTF-8, ISO-8859-1
     * 
     * @return string
     */
    public static String getApplicationCharset() {
        return resourceBundle.getString("application.charset");
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
        return "true".equals(resourceBundle.getString("log.active"));
    }

    public static void setSandboxEnvironment() {
        instance.environment = SANDBOX_ENVIRONMENT;
    }

    public static void setProductionEnvironment() {
        instance.environment = PRODUCTION_ENVIRONMENT;
    }

    public static boolean isSandboxEnvironment() {
        return SANDBOX_ENVIRONMENT.equals(instance.environment);
    }

}
