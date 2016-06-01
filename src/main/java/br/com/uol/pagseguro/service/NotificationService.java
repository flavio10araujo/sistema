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

import java.net.HttpURLConnection;
import java.util.List;

import br.com.uol.pagseguro.domain.Authorization;
import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestTransaction;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.TransactionParser;
import br.com.uol.pagseguro.parser.paymentrequest.PaymentRequestParser;
import br.com.uol.pagseguro.parser.authorization.AuthorizationParser;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * Encapsulates web service calls regarding PagSeguro notifications
 */
public class NotificationService {

    private NotificationService() {
    }

    /**
     * @var Log
     */
    private static Log log = new Log(NotificationService.class);

    /**
     * @var String
     */
    private static final String PREFIX = NotificationService.class.getSimpleName() + ".";

    /**
     * @var String
     */
    private static final String SUFFIX_BEGIN = "( %1s ) - begin";

    /**
     * @var String
     */
    private static final String SUFFIX_ERROR = " - error %2s )";

    /**
     * @var String
     */
    private static final String CHECK_TRANSACTION = "checkTransaction(notificationCode=";

    /**
     * @var String
     */
    private static final String CHECK_AUTHORIZATION = "NotificationService.CheckAuthorization(notificationCode= %1s) - error %2s";

    /**
     * @var String
     */
    private static final String CHECK_AUTHORIZATION_BEGIN = "NotificationService.CheckAuthorization(notificationCode= %s ) - begin ";

    /**
     * @var String
     */
    private static final String CHECK_PAYMENT_REQUEST_NOTIFICATION = "checkTransaction(paymentRequestNotificationCode=";

    /**
     * @param connectionData
     * @param notificationCode
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildTransactionNotificationUrl(ConnectionData connectionData, String notificationCode)
            throws PagSeguroServiceException {

        StringBuilder sb = new StringBuilder();

        sb.append(PagSeguroSystem.getNotificationUrl()).append(notificationCode).append("?")
                .append(connectionData.getCredentialsUrlQuery());

        return sb.toString();
    }

    /**
     * @param connectionData
     * @param notificationCode
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildAuthorizationNotificationUrl(ConnectionData connectionData, String notificationCode)
            throws PagSeguroServiceException {

        StringBuilder sb = new StringBuilder();

        sb.append(PagSeguroSystem.getUrlProduction()).append(PagSeguroSystem.getAuthorizationNotificationPath())
                .append(notificationCode).append("?").append(connectionData.getCredentialsUrlQuery());

        return sb.toString();
    }

    /**
     * @param connectionData
     * @param paymentRequestNotificationCode
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildPaymentRequestTransactionNotificationUrl(final ConnectionData connectionData,
            final String paymentRequestNotificationCode) throws PagSeguroServiceException {
        return connectionData.getWsPaymentRequestNotificationUrl() + "/" + paymentRequestNotificationCode + "?"
                + connectionData.getCredentialsUrlQuery();
    }

    /**
     * checkAuthorization
     * 
     * @param credentials
     * @param notificationCode
     * @throws Exception
     */
    public static Authorization checkAuthorization(Credentials credentials, String notificationCode)
            throws PagSeguroServiceException {

        NotificationService.log.info(String.format(NotificationService.CHECK_AUTHORIZATION_BEGIN, notificationCode));

        Authorization authorization = null;

        ConnectionData connectionData = new ConnectionData(credentials);
        String notificationURL = NotificationService
                .buildAuthorizationNotificationUrl(connectionData, notificationCode);

        HttpURLConnection response = NotificationService.checkNotification(credentials, notificationCode,
                notificationURL, connectionData.getServiceTimeout(), connectionData.getCharset(),
                NotificationService.CHECK_AUTHORIZATION);

        try {

            authorization = AuthorizationParser.readAuthorization(response.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
            throw new PagSeguroServiceException("Parser error ", e);

        } finally {
            response.disconnect();
        }

        NotificationService.log.info(String.format(NotificationService.CHECK_AUTHORIZATION, notificationCode,
                authorization.toString()));

        return authorization;

    }

    /**
     * checkTransaction
     * 
     * @param credentials
     * @param notificationCode
     * @throws Exception
     */
    public static Transaction checkTransaction(Credentials credentials, String notificationCode)
            throws PagSeguroServiceException {

        log.info(String.format(PREFIX + CHECK_TRANSACTION + SUFFIX_BEGIN, notificationCode));

        Transaction transaction = null;

        ConnectionData connectionData = new ConnectionData(credentials);
        String notificationURL = NotificationService.buildTransactionNotificationUrl(connectionData, notificationCode);

        HttpURLConnection response = NotificationService.checkNotification(credentials, notificationCode,
                notificationURL, connectionData.getServiceTimeout(), connectionData.getCharset(),
                NotificationService.CHECK_TRANSACTION);

        try {

            transaction = TransactionParser.readTransaction(response.getInputStream());

        } catch (Exception e) {
            throw new PagSeguroServiceException("Parser error", e);
        } finally {
            response.disconnect();
        }

        NotificationService.log.info(String.format(NotificationService.CHECK_TRANSACTION, notificationCode,
                transaction.toString()));

        return transaction;

    }

    /**
     * checkNotification
     * 
     * @param credentials
     * @param notificationCode
     * @param notificationURL
     * @param logInfo
     * @throws Exception
     */
    public static HttpURLConnection checkNotification(Credentials credentials, String notificationCode,
            String notificationURL, String serviceTimeout, String charset, String logInfo)
            throws PagSeguroServiceException {

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.get(notificationURL, serviceTimeout, charset, null);

        try {

            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {

                return response;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {

                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                log.error(String.format(PREFIX + CHECK_TRANSACTION + SUFFIX_ERROR, notificationCode,
                        exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            log.error(String.format(PREFIX + CHECK_TRANSACTION + SUFFIX_ERROR, notificationCode, e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);
        }

    }

    /**
     * checkPaymentRequest
     *
     * @param credentials
     * @param paymentRequestNotificationCode
     * @throws Exception
     */
    public static PaymentRequestTransaction checkPaymentRequestTransaction(Credentials credentials,
            String paymentRequestNotificationCode) throws PagSeguroServiceException {
        if (paymentRequestNotificationCode == null || ("").equals(paymentRequestNotificationCode.trim())
                || paymentRequestNotificationCode.contains(" ")) {
            throw new PagSeguroServiceException(HttpStatus.NOT_FOUND);
        }

        log.info(String.format(PREFIX + CHECK_PAYMENT_REQUEST_NOTIFICATION + SUFFIX_BEGIN,
                paymentRequestNotificationCode));

        ConnectionData connectionData = new ConnectionData(credentials);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;
        PaymentRequestTransaction paymentRequestTransaction = null;

        HttpURLConnection response = connection.get(NotificationService.buildPaymentRequestTransactionNotificationUrl(
                connectionData, paymentRequestNotificationCode), connectionData.getServiceTimeout(), connectionData
                .getCharset(), PagSeguroSystem.getAcceptHeaderXML());

        try {

            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {

                paymentRequestTransaction = PaymentRequestParser.readPaymentRequest(response.getInputStream());

                log.info(String.format(PREFIX + CHECK_PAYMENT_REQUEST_NOTIFICATION + SUFFIX_BEGIN,
                        paymentRequestNotificationCode, paymentRequestTransaction.toString()));

                return paymentRequestTransaction;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {

                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                log.error(String.format(PREFIX + CHECK_PAYMENT_REQUEST_NOTIFICATION + SUFFIX_ERROR,
                        paymentRequestNotificationCode, exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            log.error(String.format(PREFIX + CHECK_PAYMENT_REQUEST_NOTIFICATION + SUFFIX_ERROR,
                    paymentRequestNotificationCode, e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);

        } finally {
            response.disconnect();
        }
    }

}
