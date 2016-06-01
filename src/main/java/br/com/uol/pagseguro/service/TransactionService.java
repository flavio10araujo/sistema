package br.com.uol.pagseguro.service;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.direct.PaymentRequest;
import br.com.uol.pagseguro.domain.direct.checkout.Checkout;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.TransactionParser;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * 
 * Class Transaction Service
 */
@SuppressWarnings("deprecation")
public class TransactionService {

    /**
     * @var Log
     */
    private static Log log = new Log(TransactionService.class);

    private static String buildDirectCheckoutUrl(ConnectionData connectionData) //
            throws PagSeguroServiceException {
        return connectionData.getDirectPaymentUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    /**
     * @param connectionData
     * @return
     * @throws PagSeguroServiceException
     * 
     * @deprecated use {@link #buildDirectCheckoutUrl(ConnectionData)} instead.
     */
    @Deprecated
    private static String buildDirectPaymentRequestUrl(ConnectionData connectionData) //
            throws PagSeguroServiceException {
        return connectionData.getDirectPaymentUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    public static Transaction createTransaction(Credentials credentials, //
            Checkout checkout) //
            throws PagSeguroServiceException {
        log.info(String.format("TransactionService.createTransaction(%s) - begin", //
                checkout.toString()));

        ConnectionData connectionData = new ConnectionData(credentials);

        Map<Object, Object> data = checkout.getMap();

        String url = TransactionService.buildDirectCheckoutUrl(connectionData);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.post(url, //
                data, //
                connectionData.getServiceTimeout(), //
                connectionData.getCharset());

        try {
            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());

            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {
                Transaction transaction = TransactionParser.readTransaction(response.getInputStream());

                log.info("TransactionService.createTransaction() - end");

                return transaction;
            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {
                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                log.error(String.format("TransactionService.createTransaction() - error %s", //
                        exception.getMessage()));

                throw exception;
            } else if (HttpURLConnection.HTTP_UNAUTHORIZED == httpCodeStatus.getCode().intValue()) {
                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus);

                log.error(String.format("TransactionService.createTransaction() - error %s", //
                        exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }
        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(String.format("TransactionService.createTransaction() - error %s", //
                    e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);
        } finally {
            response.disconnect();
        }
    }

    /**
     * @param credentials
     * @param payment
     * @return
     * @throws PagSeguroServiceException
     * 
     * @deprecated use {@link #createTransaction(Credentials, Checkout)} instead.
     */
    @Deprecated
    public static Transaction createTransaction(Credentials credentials, //
            PaymentRequest payment) //
            throws PagSeguroServiceException {
        log.info(String.format("TransactionService.createTransaction(%s) - begin", //
                payment.toString()));

        ConnectionData connectionData = new ConnectionData(credentials);

        Map<Object, Object> data = payment.getMap();

        String url = TransactionService.buildDirectPaymentRequestUrl(connectionData);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.post(url, //
                data, //
                connectionData.getServiceTimeout(), //
                connectionData.getCharset());

        try {
            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());

            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {
                Transaction transaction = TransactionParser.readTransaction(response.getInputStream());

                log.info("TransactionService.createTransaction() - end");

                return transaction;
            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {
                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                log.error(String.format("TransactionService.createTransaction() - error %s", //
                        exception.getMessage()));

                throw exception;
            } else if (HttpURLConnection.HTTP_UNAUTHORIZED == httpCodeStatus.getCode().intValue()) {
                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus);

                log.error(String.format("TransactionService.createTransaction() - error %s", //
                        exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }
        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(String.format("TransactionService.createTransaction() - error %s", //
                    e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);
        } finally {
            response.disconnect();
        }
    }

}
