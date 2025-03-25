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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Helper to get values from properties
 */
@Component
public class PagSeguroSystem {

    private static final String LIB_VERSION = "2.5.2";
    private static final String LANGUAGE_ENGINE_DESCRIPTION = System.getProperty("java.version") + ":" + System.getProperty("java.vendor");

    private static String pagSeguroCheckoutServiceServicePath;
    private static String pagSeguroCheckoutServiceProductionCheckoutUrl;
    private static String pagSeguroCheckoutServiceSandboxCheckoutUrl;
    private static String pagSeguroPaymentRequestServiceServicePath;
    private static String pagSeguroPaymentRequestServiceFindByCodePath;
    private static String pagSeguroEnvironmentProductionWebserviceUrl;
    private static String pagSeguroEnvironmentSandboxWebserviceUrl;
    private static String pagSeguroPaymentServiceNfDescription;
    private static String pagSeguroPaymentServiceProductionSessions;
    private static String pagSeguroPaymentServiceSandboxSessions;
    private static String pagSeguroPaymentServiceProductionInstallments;
    private static String pagSeguroPaymentServiceSandboxInstallments;
    private static String pagSeguroPaymentServiceProductionDirectPayment;
    private static String pagSeguroPaymentServiceSandboxDirectPayment;
    private static String pagSeguroPaymentServiceProductionPaymentMethods;
    private static String pagSeguroPaymentServiceSandboxPaymentMethods;
    private static String pagSeguroAuthorizationServiceServicePath;
    private static String pagSeguroAuthorizationServiceNotificationPath;
    private static String pagSeguroAuthorizationServiceFindByCodePath;
    private static String pagSeguroAuthorizationServiceFindByDatePath;
    private static String pagSeguroAuthorizationServiceProductionAuthorizationUrl;
    private static String pagSeguroAuthorizationServiceSandboxAuthorizationUrl;
    private static String pagSeguroRecurrenceServiceServicePath;
    private static String pagSeguroRecurrenceServiceFindByCodePath;
    private static String pagSeguroRecurrenceServiceCancelByCodePath;
    private static String pagSeguroNotificationServiceProductionUrl;
    private static String pagSeguroNotificationServiceSandboxUrl;
    private static String pagSeguroNotificationServicePaymentRequest;
    private static String pagSeguroTransactionSearchServiceProductionUrl;
    private static String pagSeguroTransactionSearchServiceSandboxUrl;
    private static String pagSeguroContentTypeFormUrlEncoded;
    private static String pagSeguroAcceptHeaderXML;
    private static String pagSeguroEncoding;
    private static String pagSeguroServiceTimeout;

    private PagSeguroSystem() {
    }

    @Value("${pagSeguro.checkoutService.v1.servicePath}")
    public void setPagSeguroCheckoutServiceServicePath(String pagSeguroCheckoutServiceServicePath) {
        PagSeguroSystem.pagSeguroCheckoutServiceServicePath = pagSeguroCheckoutServiceServicePath;
    }

    @Value("${pagSeguro.checkoutService.production.checkoutUrl}")
    public void setPagSeguroCheckoutServiceProductionCheckoutUrl(String pagSeguroCheckoutServiceProductionCheckoutUrl) {
        PagSeguroSystem.pagSeguroCheckoutServiceProductionCheckoutUrl = pagSeguroCheckoutServiceProductionCheckoutUrl;
    }

    @Value("${pagSeguro.checkoutService.sandbox.checkoutUrl}")
    public void setPagSeguroCheckoutServiceSandboxCheckoutUrl(String pagSeguroCheckoutServiceSandboxCheckoutUrl) {
        PagSeguroSystem.pagSeguroCheckoutServiceSandboxCheckoutUrl = pagSeguroCheckoutServiceSandboxCheckoutUrl;
    }

    @Value("${pagSeguro.paymentRequestService.servicePath}")
    public void setPagSeguroPaymentRequestServiceServicePath(String pagSeguroPaymentRequestServiceServicePath) {
        PagSeguroSystem.pagSeguroPaymentRequestServiceServicePath = pagSeguroPaymentRequestServiceServicePath;
    }

    @Value("${pagSeguro.paymentRequestService.findByCodePath}")
    public void setPagSeguroPaymentRequestServiceFindByCodePath(String pagSeguroPaymentRequestServiceFindByCodePath) {
        PagSeguroSystem.pagSeguroPaymentRequestServiceFindByCodePath = pagSeguroPaymentRequestServiceFindByCodePath;
    }

    @Value("${pagSeguro.environment.production.v1.webserviceUrl}")
    public void setPagSeguroEnvironmentProductionWebserviceUrl(String pagSeguroEnvironmentProductionWebserviceUrl) {
        PagSeguroSystem.pagSeguroEnvironmentProductionWebserviceUrl = pagSeguroEnvironmentProductionWebserviceUrl;
    }

    @Value("${pagSeguro.environment.sandbox.v1.webserviceUrl}")
    public void setPagSeguroEnvironmentSandboxWebserviceUrl(String pagSeguroEnvironmentSandboxWebserviceUrl) {
        PagSeguroSystem.pagSeguroEnvironmentSandboxWebserviceUrl = pagSeguroEnvironmentSandboxWebserviceUrl;
    }

    @Value("${pagSeguro.paymentService.nf.description}")
    public void setPagSeguroPaymentServiceNfDescription(String pagSeguroPaymentServiceNfDescription) {
        PagSeguroSystem.pagSeguroPaymentServiceNfDescription = pagSeguroPaymentServiceNfDescription;
    }

    @Value("${pagSeguro.paymentService.production.sessions}")
    public void setPagSeguroPaymentServiceProductionSessions(String pagSeguroPaymentServiceProductionSessions) {
        PagSeguroSystem.pagSeguroPaymentServiceProductionSessions = pagSeguroPaymentServiceProductionSessions;
    }

    @Value("${pagSeguro.paymentService.sandbox.sessions}")
    public void setPagSeguroPaymentServiceSandboxSessions(String pagSeguroPaymentServiceSandboxSessions) {
        PagSeguroSystem.pagSeguroPaymentServiceSandboxSessions = pagSeguroPaymentServiceSandboxSessions;
    }

    @Value("${pagSeguro.paymentService.production.installments}")
    public void setPagSeguroPaymentServiceProductionInstallments(String pagSeguroPaymentServiceProductionInstallments) {
        PagSeguroSystem.pagSeguroPaymentServiceProductionInstallments = pagSeguroPaymentServiceProductionInstallments;
    }

    @Value("${pagSeguro.paymentService.sandbox.installments}")
    public void setPagSeguroPaymentServiceSandboxInstallments(String pagSeguroPaymentServiceSandboxInstallments) {
        PagSeguroSystem.pagSeguroPaymentServiceSandboxInstallments = pagSeguroPaymentServiceSandboxInstallments;
    }

    @Value("${pagSeguro.paymentService.production.directPayment}")
    public void setPagSeguroPaymentServiceProductionDirectPayment(String pagSeguroPaymentServiceProductionDirectPayment) {
        PagSeguroSystem.pagSeguroPaymentServiceProductionDirectPayment = pagSeguroPaymentServiceProductionDirectPayment;
    }

    @Value("${pagSeguro.paymentService.sandbox.directPayment}")
    public void setPagSeguroPaymentServiceSandboxDirectPayment(String pagSeguroPaymentServiceSandboxDirectPayment) {
        PagSeguroSystem.pagSeguroPaymentServiceSandboxDirectPayment = pagSeguroPaymentServiceSandboxDirectPayment;
    }

    @Value("${pagSeguro.paymentService.production.paymentMethods}")
    public void setPagSeguroPaymentServiceProductionPaymentMethods(String pagSeguroPaymentServiceProductionPaymentMethods) {
        PagSeguroSystem.pagSeguroPaymentServiceProductionPaymentMethods = pagSeguroPaymentServiceProductionPaymentMethods;
    }

    @Value("${pagSeguro.paymentService.sandbox.paymentMethods}")
    public void setPagSeguroPaymentServiceSandboxPaymentMethods(String pagSeguroPaymentServiceSandboxPaymentMethods) {
        PagSeguroSystem.pagSeguroPaymentServiceSandboxPaymentMethods = pagSeguroPaymentServiceSandboxPaymentMethods;
    }

    @Value("${pagSeguro.authorizationService.servicePath}")
    public void setPagSeguroAuthorizationServiceServicePath(String pagSeguroAuthorizationServiceServicePath) {
        PagSeguroSystem.pagSeguroAuthorizationServiceServicePath = pagSeguroAuthorizationServiceServicePath;
    }

    @Value("${pagSeguro.authorizationService.notificationPath}")
    public void setPagSeguroAuthorizationServiceNotificationPath(String pagSeguroAuthorizationServiceNotificationPath) {
        PagSeguroSystem.pagSeguroAuthorizationServiceNotificationPath = pagSeguroAuthorizationServiceNotificationPath;
    }

    @Value("${pagSeguro.authorizationService.findByCodePath}")
    public void setPagSeguroAuthorizationServiceFindByCodePath(String pagSeguroAuthorizationServiceFindByCodePath) {
        PagSeguroSystem.pagSeguroAuthorizationServiceFindByCodePath = pagSeguroAuthorizationServiceFindByCodePath;
    }

    @Value("${pagSeguro.authorizationService.findByDatePath}")
    public void setPagSeguroAuthorizationServiceFindByDatePath(String pagSeguroAuthorizationServiceFindByDatePath) {
        PagSeguroSystem.pagSeguroAuthorizationServiceFindByDatePath = pagSeguroAuthorizationServiceFindByDatePath;
    }

    @Value("${pagSeguro.authorizationService.production.authorizationUrl}")
    public void setPagSeguroAuthorizationServiceProductionAuthorizationUrl(String pagSeguroAuthorizationServiceProductionAuthorizationUrl) {
        PagSeguroSystem.pagSeguroAuthorizationServiceProductionAuthorizationUrl = pagSeguroAuthorizationServiceProductionAuthorizationUrl;
    }

    @Value("${pagSeguro.authorizationService.sandbox.authorizationUrl}")
    public void setPagSeguroAuthorizationServiceSandboxAuthorizationUrl(String pagSeguroAuthorizationServiceSandboxAuthorizationUrl) {
        PagSeguroSystem.pagSeguroAuthorizationServiceSandboxAuthorizationUrl = pagSeguroAuthorizationServiceSandboxAuthorizationUrl;
    }

    @Value("${pagSeguro.recurrenceService.servicePath}")
    public void setPagSeguroRecurrenceServiceServicePath(String pagSeguroRecurrenceServiceServicePath) {
        PagSeguroSystem.pagSeguroRecurrenceServiceServicePath = pagSeguroRecurrenceServiceServicePath;
    }

    @Value("${pagSeguro.recurrenceService.findByCodePath}")
    public void setPagSeguroRecurrenceServiceFindByCodePath(String pagSeguroRecurrenceServiceFindByCodePath) {
        PagSeguroSystem.pagSeguroRecurrenceServiceFindByCodePath = pagSeguroRecurrenceServiceFindByCodePath;
    }

    @Value("${pagSeguro.recurrenceService.cancelByCodePath}")
    public void setPagSeguroRecurrenceServiceCancelByCodePath(String pagSeguroRecurrenceServiceCancelByCodePath) {
        PagSeguroSystem.pagSeguroRecurrenceServiceCancelByCodePath = pagSeguroRecurrenceServiceCancelByCodePath;
    }

    @Value("${pagSeguro.notificationService.production.url}")
    public void setPagSeguroNotificationServiceProductionUrl(String pagSeguroNotificationServiceProductionUrl) {
        PagSeguroSystem.pagSeguroNotificationServiceProductionUrl = pagSeguroNotificationServiceProductionUrl;
    }

    @Value("${pagSeguro.notificationService.sandbox.url}")
    public void setPagSeguroNotificationServiceSandboxUrl(String pagSeguroNotificationServiceSandboxUrl) {
        PagSeguroSystem.pagSeguroNotificationServiceSandboxUrl = pagSeguroNotificationServiceSandboxUrl;
    }

    @Value("${pagSeguro.notificationService.paymentRequest}")
    public void setPagSeguroNotificationServicePaymentRequest(String pagSeguroNotificationServicePaymentRequest) {
        PagSeguroSystem.pagSeguroNotificationServicePaymentRequest = pagSeguroNotificationServicePaymentRequest;
    }

    @Value("${pagSeguro.transactionSearchService.production.url}")
    public void setPagSeguroTransactionSearchServiceProductionUrl(String pagSeguroTransactionSearchServiceProductionUrl) {
        PagSeguroSystem.pagSeguroTransactionSearchServiceProductionUrl = pagSeguroTransactionSearchServiceProductionUrl;
    }

    @Value("${pagSeguro.transactionSearchService.sandbox.url}")
    public void setPagSeguroTransactionSearchServiceSandboxUrl(String pagSeguroTransactionSearchServiceSandboxUrl) {
        PagSeguroSystem.pagSeguroTransactionSearchServiceSandboxUrl = pagSeguroTransactionSearchServiceSandboxUrl;
    }

    @Value("${pagSeguro.contentType.formUrlEncoded}")
    public void setPagSeguroContentTypeFormUrlEncoded(String pagSeguroContentTypeFormUrlEncoded) {
        PagSeguroSystem.pagSeguroContentTypeFormUrlEncoded = pagSeguroContentTypeFormUrlEncoded;
    }

    @Value("${pagSeguro.acceptHeader.xml}")
    public void setPagSeguroAcceptHeaderXML(String pagSeguroAcceptHeaderXML) {
        PagSeguroSystem.pagSeguroAcceptHeaderXML = pagSeguroAcceptHeaderXML;
    }

    @Value("${pagSeguro.encoding}")
    public void setPagSeguroEncoding(String pagSeguroEncoding) {
        PagSeguroSystem.pagSeguroEncoding = pagSeguroEncoding;
    }

    @Value("${pagSeguro.serviceTimeout}")
    public void setPagSeguroServiceTimeout(String pagSeguroServiceTimeout) {
        PagSeguroSystem.pagSeguroServiceTimeout = pagSeguroServiceTimeout;
    }

    private static boolean isProduction() {
        return "production".equals(PagSeguroConfig.getEnvironment());
    }

    public static String getCheckoutUrl() {
        if (isProduction()) {
            return pagSeguroCheckoutServiceProductionCheckoutUrl;
        }

        return pagSeguroCheckoutServiceSandboxCheckoutUrl;
    }

    /**
     * Get Url to create a payment request
     *
     * @return string
     */
    public static String getUrlPaymentRequest() {
        //return resourceBundle.getString("paymentRequestService." + PagSeguroConfig.getEnvironment() + ".paymentRequestUrl");
        return "";
    }

    /**
     * Get Url payment production
     *
     * @return string
     */
    public static String getUrlProduction() {
        if (isProduction()) {
            return pagSeguroEnvironmentProductionWebserviceUrl;
        }

        return pagSeguroEnvironmentSandboxWebserviceUrl;
    }

    /**
     * Get Url to create a session
     *
     * @return string
     */
    public static String getUrlSessions() {
        if (isProduction()) {
            return pagSeguroPaymentServiceProductionSessions;
        }

        return pagSeguroPaymentServiceSandboxSessions;
    }

    /**
     * Get Url to Installments
     *
     * @return string
     */
    public static String getUrlInstallments() {
        if (isProduction()) {
            return pagSeguroPaymentServiceProductionInstallments;
        }

        return pagSeguroPaymentServiceSandboxInstallments;
    }

    /**
     * Get Url to Direct Payment
     *
     * @return string
     */
    public static String getUrlDirectPayment() {
        if (isProduction()) {
            return pagSeguroPaymentServiceProductionDirectPayment;
        }

        return pagSeguroPaymentServiceSandboxDirectPayment;
    }

    /**
     * Get Url to Authorization
     *
     * @return string
     */
    public static String getUrlAuthorization() {
        if (isProduction()) {
            return pagSeguroAuthorizationServiceProductionAuthorizationUrl;
        }

        return pagSeguroAuthorizationServiceSandboxAuthorizationUrl;
    }

    /**
     * Get Url to Payment-Methods
     *
     * @return string
     */
    public static String getUrlPaymentMethods() {
        if (isProduction()) {
            return pagSeguroPaymentServiceProductionPaymentMethods;
        }

        return pagSeguroPaymentServiceSandboxPaymentMethods;
    }

    /**
     * Get checkout service path
     *
     * @return string
     */
    public static String getCheckoutServicePath() {
        return pagSeguroCheckoutServiceServicePath;
    }

    public static String getPaymentRequestServicePath() {
        return pagSeguroPaymentRequestServiceServicePath;
    }

    public static String getPaymentRequestFindByCodePath() {
        return pagSeguroPaymentRequestServiceFindByCodePath;
    }

    public static String getRecurrenceServicePath() {
        return pagSeguroRecurrenceServiceServicePath;
    }

    public static String getRecurrenceFindByCodePath() {
        return pagSeguroRecurrenceServiceFindByCodePath;
    }

    public static String getRecurrenceCancelByCodePath() {
        return pagSeguroRecurrenceServiceCancelByCodePath;
    }

    /**
     * Get authorization service path
     *
     * @return string
     */
    public static String getAuthorizationServicePath() {
        return pagSeguroAuthorizationServiceServicePath;
    }

    public static String getAuthorizationNotificationPath() {
        return pagSeguroAuthorizationServiceNotificationPath;
    }

    public static String getAuthorizationFindByCodePath() {
        return pagSeguroAuthorizationServiceFindByCodePath;
    }

    public static String getAuthorizationFindByDatePath() {
        return pagSeguroAuthorizationServiceFindByDatePath;
    }

    public static String getServiceTimeout() {
        return pagSeguroServiceTimeout;
    }

    public static String getNotificationUrl() {
        if (isProduction()) {
            return pagSeguroNotificationServiceProductionUrl;
        }

        return pagSeguroNotificationServiceSandboxUrl;
    }

    public static String getPaymentRequestNotificationUrl() {
        return pagSeguroNotificationServicePaymentRequest;
    }

    public static String getUrlNotification() {
        //return resourceBundle.getString("url.notification");
        return "";
    }

    public static String getTransactionSearchUrl() {
        if (isProduction()) {
            return pagSeguroTransactionSearchServiceProductionUrl;
        }

        return pagSeguroTransactionSearchServiceSandboxUrl;
    }

    public static String getUrlPaymentRedir() {
        //return resourceBundle.getString("url.payment.redir");
        return "";
    }

    public static String getContentTypeFormUrlEncoded() {
        return pagSeguroContentTypeFormUrlEncoded;
    }

    public static String getAcceptHeaderXML() {
        return pagSeguroAcceptHeaderXML;
    }

    public static String getPagSeguroEncoding() {
        return pagSeguroEncoding;
    }

    public static String getLibversion() {
        return LIB_VERSION;
    }

    public static String getLanguageEnginedescription() {
        return LANGUAGE_ENGINE_DESCRIPTION;
    }

    public static String getPagSeguroPaymentServiceNfDescription() {
        return pagSeguroPaymentServiceNfDescription;
    }
}
