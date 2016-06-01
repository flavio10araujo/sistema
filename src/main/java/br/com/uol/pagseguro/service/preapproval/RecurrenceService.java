/*
 ************************************************************************
 Copyright [2014] [PagSeguro Internet Ltda.]

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

package br.com.uol.pagseguro.service.preapproval;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.preapproval.Recurrence;
import br.com.uol.pagseguro.domain.preapproval.RecurrenceCancelTransaction;
import br.com.uol.pagseguro.domain.preapproval.RecurrenceTransaction;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.preapproval.RecurrenceParser;
import br.com.uol.pagseguro.properties.PagSeguroSystem;
import br.com.uol.pagseguro.service.ConnectionData;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * 
 * Class Recurrence Service
 */
public class RecurrenceService {

    private RecurrenceService() {

    }

    /**
     * PagSeguro Log tool
     * 
     * @see Log
     */
    private static final Log log = new Log(RecurrenceService.class);

    /**
     * @var String
     */
    private static final String PREFIX = RecurrenceService.class.getSimpleName() + ".";

    /**
     * @var String
     */
    private static final String SUFFIX_BEGIN = "( %1s ) - begin";

    /**
     * @var String
     */
    private static final String SUFFIX_END = " - end %2s )";

    /**
     * @var String
     */
    private static final String SUFFIX_ERROR = " - error %2s )";

    /**
     * @var String
     */
    private static final String REGISTER = "Register";

    /**
     * @var String
     */
    private static final String FIND_BY_CODE = "FindByCode - ";

    /**
     * @var String
     */
    private static final String CANCEL_BY_CODE = "CancelByCode - ";

    /**
     * 
     * @param connectionData
     * @return string
     * @throws PagSeguroServiceException
     */
    public static String buildRecurrenceUrl(ConnectionData connectionData) throws PagSeguroServiceException {
        return connectionData.getWSRecurrenceUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    /**
     * Build Find Url By Code
     * 
     * @param connectionData
     * @param recurrenceCode
     * @return string
     * @throws PagSeguroServiceException
     */
    private static String buildRecurrenceFindUrlByCode(ConnectionData connectionData, String recurrenceCode)
            throws PagSeguroServiceException {
        return connectionData.getWSRecurrenceFindByCodeUrl() + "/" + recurrenceCode + "?"
                + connectionData.getCredentialsUrlQuery();
    }

    /**
     * Build Cancel Url By Code
     * 
     * @param connectionData
     * @param recurrenceCode
     * @return string
     * @throws PagSeguroServiceException
     */
    private static String buildRecurrenceCancelByCodeUrl(ConnectionData connectionData, String recurrenceCode)
            throws PagSeguroServiceException {
        return connectionData.getWSRecurrenceCancelByCodeUrl() + "/" + recurrenceCode + "?"
                + connectionData.getCredentialsUrlQuery();
    }

    /**
     * 
     * @param credentials
     * @param recurrence
     * @return string
     * @throws Exception
     */
    public static String createRecurrence(Credentials credentials, Recurrence recurrence)
            throws PagSeguroServiceException {

        log.info(String.format(PREFIX + REGISTER + SUFFIX_BEGIN, recurrence.toString()));

        ConnectionData connectionData = new ConnectionData(credentials);

        Map<Object, Object> data = RecurrenceParser.getData(recurrence);

        String url = buildRecurrenceUrl(connectionData);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.post(url, data, connectionData.getServiceTimeout(),
                connectionData.getCharset(), PagSeguroSystem.getAcceptHeaderXML());

        try {

            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());
            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {
                String code = RecurrenceParser.readSuccessXml(response);

                log.info(String.format(PREFIX + REGISTER + SUFFIX_END, recurrence.toString(), code));

                return code;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {

                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                log.error(String.format(PREFIX + REGISTER + SUFFIX_ERROR, recurrence.toString(), exception.getMessage()));

                throw exception;

            } else {

                throw new PagSeguroServiceException(httpCodeStatus);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            log.error(String.format(PREFIX + REGISTER + SUFFIX_ERROR, recurrence.toString(), e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);

        } finally {
            response.disconnect();
        }
    }

    /**
     * 
     * @param credentials
     * @param recurrenceCode
     * @return PaymentRequest
     * @throws Exception
     */
    public static RecurrenceTransaction findByCode(Credentials credentials, String recurrenceCode)
            throws PagSeguroServiceException {

        log.info(String.format(PREFIX + FIND_BY_CODE + SUFFIX_BEGIN, recurrenceCode));

        ConnectionData connectionData = new ConnectionData(credentials);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpStatusCode = null;

        HttpURLConnection response = connection.get(buildRecurrenceFindUrlByCode(connectionData, recurrenceCode),
                connectionData.getServiceTimeout(), connectionData.getCharset(), PagSeguroSystem.getAcceptHeaderXML());

        try {

            httpStatusCode = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpStatusCode.getCode().intValue()) {

                RecurrenceTransaction recurrenceTransaction = RecurrenceParser
                        .readRecurrence(response.getInputStream());

                log.info(FIND_BY_CODE + recurrenceCode + recurrenceTransaction.toString());

                return recurrenceTransaction;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpStatusCode.getCode().intValue()) {

                List<Error> listErrors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpStatusCode, listErrors);

                log.error(String.format(PREFIX + FIND_BY_CODE + SUFFIX_ERROR, recurrenceCode, exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpStatusCode);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            log.error(String.format(PREFIX + FIND_BY_CODE + SUFFIX_END, recurrenceCode, e.getMessage()));

            throw new PagSeguroServiceException(httpStatusCode, e);

        } finally {
            response.disconnect();
        }
    }

    /**
     * 
     * @param credentials
     * @param recurrenceCode
     * @return
     * @throws PagSeguroServiceException
     */
    public static RecurrenceCancelTransaction cancelRecurrenceByCode(Credentials credentials, String recurrenceCode)
            throws PagSeguroServiceException {

        log.info(String.format(PREFIX + CANCEL_BY_CODE + SUFFIX_BEGIN, recurrenceCode));

        ConnectionData connectionData = new ConnectionData(credentials);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpStatusCode = null;

        HttpURLConnection response = connection.httpRequestMethod(
                buildRecurrenceCancelByCodeUrl(connectionData, recurrenceCode), connectionData.getServiceTimeout(),
                connectionData.getCharset(), "POST", PagSeguroSystem.getAcceptHeaderXML());

        try {

            httpStatusCode = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpStatusCode.getCode().intValue()) {

                RecurrenceCancelTransaction cancelTransaction = RecurrenceParser.readCancelXml(response
                        .getInputStream());

                log.info(CANCEL_BY_CODE + recurrenceCode);

                return cancelTransaction;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpStatusCode.getCode().intValue()) {

                List<Error> listErrors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpStatusCode, listErrors);

                log.error(String.format(PREFIX + CANCEL_BY_CODE + SUFFIX_ERROR, recurrenceCode, exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpStatusCode);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            log.error(String.format(PREFIX + CANCEL_BY_CODE + SUFFIX_END, recurrenceCode, e.getMessage()));

            throw new PagSeguroServiceException(httpStatusCode, e);

        } finally {
            response.disconnect();
        }
    }
}
