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
package br.com.uol.pagseguro.service;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.helper.PagSeguroUtil;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.properties.PagSeguroSystem;

public class ConnectionData {

    private Credentials credentials;

    private String webServiceUrl;

    private String wsPaymentRequestUrl;

    private String wsPaymentRequestFindByCodeUrl;

    private String wsPaymentRequestNotificationUrl;

    private String wsRecurrenceUrl;

    private String wsRecurrenceFindByCodeUrl;

    private String wsRecurrenceCancelByCodeUrl;
    
    private String wsAuthorizationUrl;
    
    private String wsAuthorizationNotificationUrl;
    
    private String wsAuthorizationFindByCodeUrl;
    
    private String wsAuthorizationFindByDateUrl;

    private String sessionsUrl;

    private String installmentsUrl;

    private String directPayment;
    
    private String authorizationUrl;

    private String paymentMethodsUrl;

    private String checkoutServicePath;

    private String serviceTimeout;

    private String charset;

    public ConnectionData(Credentials credentials) {
        this.credentials = credentials;

        this.webServiceUrl = validUrlWebService();

        this.wsPaymentRequestUrl = validUrlWSPaymentRequest();
        this.wsAuthorizationNotificationUrl = validUrlWSAuthorizationNotification();
        this.wsAuthorizationFindByCodeUrl = validUrlWSAuthorizationFindByCode();
        this.wsAuthorizationFindByDateUrl = validUrlWSAuthorizationFindByDate();
        this.wsPaymentRequestFindByCodeUrl = validUrlWSPaymentRequestFindByCode();
        this.wsPaymentRequestNotificationUrl = validUrlWSPaymentRequestByNotificationCode();

        this.wsRecurrenceUrl = validUrlWSRecurrence();
        this.wsRecurrenceFindByCodeUrl = validUrlWSRecurrenceFindByCode();
        this.wsRecurrenceCancelByCodeUrl = validUrlWSRecurrenceCancelByCode();

        this.wsAuthorizationUrl = validUrlWSAuthorization();

        this.charset = PagSeguroConfig.getApplicationCharset();
        this.serviceTimeout = PagSeguroSystem.getServiceTimeout();

        this.sessionsUrl = PagSeguroSystem.getUrlSessions();
        this.installmentsUrl = PagSeguroSystem.getUrlInstallments();
        this.directPayment = PagSeguroSystem.getUrlDirectPayment();
        this.authorizationUrl = PagSeguroSystem.getUrlAuthorization();
        this.paymentMethodsUrl = PagSeguroSystem.getUrlPaymentMethods();

        this.checkoutServicePath = PagSeguroSystem.getCheckoutServicePath();
    }

    /**
     * Get Service Url
     * 
     * @return string
     */
    public String getServiceUrl() {
        return this.getWebServiceUrl() + this.getServicePath();
    }

    /**
     * Get Sessions Url
     * 
     * @return string
     */
    public String getSessionsUrl() {
        return this.sessionsUrl;
    }

    /**
     * Get Installments Url
     * 
     * @return string
     */
    public String getInstallmentsUrl() {
        return this.installmentsUrl;
    }

    /**
     * Get Direct Payment Url
     * 
     * @return string
     */
    public String getDirectPaymentUrl() {
        return this.directPayment;
    }
    
    /**
     * Get Authorization Payment Url
     * 
     * @return string
     */
    public String getAuthorizationUrl() {
        return this.authorizationUrl;
    }
    
    /**
     * @return the paymentMethodsUrl
     */
    public String getPaymentMethodsUrl() {
        return paymentMethodsUrl;
    }

    /**
     * Create url
     * 
     * @return string
     * @throws PagSeguroServiceException
     */
    public String getCredentialsUrlQuery() throws PagSeguroServiceException {
        return PagSeguroUtil.urlQuery(this.getCredentials().getAttributes());
    }

    /**
     * Valid url web service production or development
     * 
     * @return string
     */
    private String validUrlWebService() {

        String url = PagSeguroSystem.getUrlProduction();

        return url + PagSeguroSystem.getCheckoutServicePath();

    }

    /**
     * Valid url web service production or development for payment request
     * 
     * @return string
     */
    private String validUrlWSPaymentRequest() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getPaymentRequestServicePath();
    }

    /**
     * Valid url web service production or development for finding a payment request transaction
     * 
     * @return string
     */
    private String validUrlWSPaymentRequestFindByCode() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getPaymentRequestFindByCodePath();
    }
    
    /**
     * Valid url web service production or development for authorization request
     * 
     * @return string
     */
    private String validUrlWSAuthorization() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getAuthorizationServicePath();
    }
    
    /**
     * Valid url web service production or development for authorization notification request
     * 
     * @return string
     */
    private String validUrlWSAuthorizationNotification() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getAuthorizationNotificationPath();
    }
    
    /**
     * Valid url web service production or development for authorization find by coderequest
     * 
     * @return string
     */
    private String validUrlWSAuthorizationFindByCode() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getAuthorizationFindByCodePath();
    }
    
    /**
     * Valid url web service production or development for authorization find all request
     * 
     * @return string
     */
    private String validUrlWSAuthorizationFindByDate() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getAuthorizationFindByDatePath();
    }

    /**
     * Valid url web service production or development for finding a payment request by notification code
     *
     * @return string
     */
    private String validUrlWSPaymentRequestByNotificationCode() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getPaymentRequestNotificationUrl();
    }

    /**
     * Valid url web service production or development for recurrence
     * 
     * @return string
     */
    private String validUrlWSRecurrence() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getRecurrenceServicePath();
    }

    /**
     * Valid url web service production or development for finding a recurrence transaction
     * 
     * @return string
     */
    private String validUrlWSRecurrenceFindByCode() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getRecurrenceFindByCodePath();
    }

    /**
     * Valid url web service production or development for canceling a recurrence transaction
     * 
     * @return string
     */
    private String validUrlWSRecurrenceCancelByCode() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getRecurrenceCancelByCodePath();
    }

    /**
     * @return the credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * @param credentials
     *            the credentials to set
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * @return the webServiceUrl
     */
    public String getWebServiceUrl() {
        return webServiceUrl;
    }

    /**
     * @return the wsPaymentRequestUrl
     */
    public String getWSPaymentRequestUrl() {
        return wsPaymentRequestUrl;
    }

    /**
     * @return the wsPaymentRequestFindByCodeUrl
     */
    public String getWSPaymentRequestFindByCodeUrl() {
        return wsPaymentRequestFindByCodeUrl;
    }
    
    /**
     * @return the wsAuthorization Url
     */
    public String getWSAuthorizationUrl() {
        return wsAuthorizationUrl;
    }
    
    /**
     * @return the wsAuthorizationNotification Url
     */
    public String getWSAuthorizationNotificationUrl() {
        return wsAuthorizationNotificationUrl;
    }
    
    /**
     * @return the wsAuthorizationFindByCode Url
     */
    public String getWSAuthorizationFindByCodeUrl() {
        return wsAuthorizationFindByCodeUrl;
    }
    
    /**
     * @return the wsAuthorizationFindAll Url
     */
    public String getWSAuthorizationFindByDateUrl() {
        return wsAuthorizationFindByDateUrl;
    }

    /**
     * @return the wsPaymentRequestNotificationUrl
     */
    public String getWsPaymentRequestNotificationUrl() {
        return wsPaymentRequestNotificationUrl;
    }

    /**
     * @return the wsRecurrenceUrl
     */
    public String getWSRecurrenceUrl() {
        return wsRecurrenceUrl;
    }

    /**
     * @return the wsRecurrenceFindByCodeUrl
     */
    public String getWSRecurrenceFindByCodeUrl() {
        return wsRecurrenceFindByCodeUrl;
    }

    /**
     * @return the wsRecurrenceCancelByCodeUrl
     */
    public String getWSRecurrenceCancelByCodeUrl() {
        return wsRecurrenceCancelByCodeUrl;
    }

    /**
     * @param webServiceUrl
     *            the webServiceUrl to set
     */
    public void setWebServiceUrl(String webServiceUrl) {
        this.webServiceUrl = webServiceUrl;
    }

    /**
     * @param wsPaymentRequestFindByCodeUrl
     *            the wsPaymentRequestFindByCodeUrl to set
     */
    public void setWSPaymentRequestFindByCodeUrl(String wsPaymentRequestFindByCodeUrl) {
        this.wsPaymentRequestFindByCodeUrl = wsPaymentRequestFindByCodeUrl;
    }

    /**
     * @param wsPaymentRequestUrl
     *            the wsPaymentRequestUrl to set
     */
    public void setWSPaymentRequestUrl(String wsPaymentRequestUrl) {
        this.wsPaymentRequestUrl = wsPaymentRequestUrl;
    }
    
    /**
     * @param wsAuthorizationUrl
     *            the wsAuthorizationUrl to set
     */
    public void setWSAuthorizationUrl(String wsAuthorizationUrl) {
        this.wsAuthorizationUrl = wsAuthorizationUrl;
    }

    /**
     * @param wsRecurrenceUrl
     *            the wsRecurrenceUrl to set
     */
    public void setWSRecurrenceUrl(String wsRecurrenceUrl) {
        this.wsRecurrenceUrl = wsRecurrenceUrl;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset
     *            the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the servicePath
     */
    public String getServicePath() {
        return checkoutServicePath;
    }

    /**
     * @param servicePath
     *            the servicePath to set
     */
    public void setServicePath(String servicePath) {
        this.checkoutServicePath = servicePath;
    }

    /**
     * @return the serviceTimeout
     */
    public String getServiceTimeout() {
        return serviceTimeout;
    }

    /**
     * @param serviceTimeout
     *            the serviceTimeout to set
     */
    public void setServiceTimeout(String serviceTimeout) {
        this.serviceTimeout = serviceTimeout;
    }

    public String getCheckoutUrl() {
        return PagSeguroSystem.getCheckoutUrl();
    }

    public String getTransactionSearchUrl() {
        return PagSeguroSystem.getTransactionSearchUrl();
    }
}
