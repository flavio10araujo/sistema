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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionSearchResult;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.TransactionParser;
import br.com.uol.pagseguro.parser.TransactionSearchResulParser;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * Encapsulates web service calls to search for PagSeguro transactions
 */
public class TransactionSearchService {

    private TransactionSearchService() {
    }

    /**
     * PagSeguro Log tool
     * 
     * @see Log
     */
    private static Log log = new Log(TransactionSearchService.class);

    /**
     * @var DATE_FORMAT
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"; // "2011-04-01T08:30"

    /**
     * @var String
     */
    private static final String SEARCH_BY_CODE = "TransactionSearchService.SearchByCode(transactionCode= %1s) - error %2s";

    /**
     * @var String
     */
    private static final String SEARCH_BY_DATE_BEGIN = "TransactionSearchService.SearchByDate(initialDate= %1s , finalDate= %2s ) - begin";

    /**
     * @var String
     */
    private static final String SEARCH_BY_DATE = "TransactionSearchService.SearchByDate(initialDate= %1s , finalDate= %2s) - end %3s";

    /**
     * Build Search Url By Code
     * 
     * @param connectionData
     * @param transactionCode
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildSearchUrlByCode(ConnectionData connectionData, String transactionCode)
            throws PagSeguroServiceException {
        return connectionData.getTransactionSearchUrl() + "/" + transactionCode + "?"
                + connectionData.getCredentialsUrlQuery();
    }

    /**
     * Build Search Url By Date
     * 
     * @param connectionData
     * @param initialDate
     * @param finalDate
     * @param page
     * @param maxPageResults
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildSearchUrlByDate(ConnectionData connectionData, String initialDate, String finalDate,
            Integer page, Integer maxPageResults) throws PagSeguroServiceException {

        StringBuilder sb = new StringBuilder();
        sb.append(connectionData.getTransactionSearchUrl());
        sb.append("?initialDate=" + (initialDate != null ? initialDate : ""));
        sb.append("&finalDate=" + (finalDate != null ? finalDate : ""));

        if (page != null) {
            sb.append("&page=" + page);
        }

        if (maxPageResults != null) {
            sb.append("&maxPageResults=" + maxPageResults);
        }

        sb.append("&" + connectionData.getCredentialsUrlQuery());

        return sb.toString();
    }

    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate)
            throws PagSeguroServiceException {
        return searchByDateCore(credentials, initialDate, new Date(Long.MAX_VALUE), 0, 0);
    }

    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate, int pageNumber)
            throws PagSeguroServiceException {
        return searchByDateCore(credentials, initialDate, new Date(Long.MAX_VALUE), pageNumber, 0);
    }

    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate, int pageNumber,
            int resultsPerPage) throws PagSeguroServiceException {
        return searchByDateCore(credentials, initialDate, new Date(Long.MAX_VALUE), pageNumber, resultsPerPage);
    }

    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate, Date finalDate)
            throws PagSeguroServiceException {
        return searchByDateCore(credentials, initialDate, finalDate, 0, 0);
    }

    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate, Date finalDate,
            int pageNumber) throws PagSeguroServiceException {
        return searchByDateCore(credentials, initialDate, finalDate, pageNumber, 0);
    }

    public static TransactionSearchResult searchByDate(Credentials credentials, Date initialDate, Date finalDate,
            int pageNumber, int resultsPerPage) throws PagSeguroServiceException {
        return searchByDateCore(credentials, initialDate, finalDate, pageNumber, resultsPerPage);
    }

    /**
     * Finds a transaction with a matching transaction code
     * 
     * @param credentials
     * @param transactionCode
     * @return
     * @throws Exception
     */
    public static Transaction searchByCode(Credentials credentials, String transactionCode)
            throws PagSeguroServiceException {

        TransactionSearchService.log.info("TransactionSearchService.SearchByCode(transactionCode=" + transactionCode
                + ") - begin");

        ConnectionData connectionData = new ConnectionData(credentials);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpStatusCode = null;

        HttpURLConnection response = connection.get(
                TransactionSearchService.buildSearchUrlByCode(connectionData, transactionCode),
                connectionData.getServiceTimeout(), connectionData.getCharset(), null);

        try {

            httpStatusCode = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpStatusCode.getCode().intValue()) {

                Transaction transaction = TransactionParser.readTransaction(response.getInputStream());

                TransactionSearchService.log.info(String.format(TransactionSearchService.SEARCH_BY_CODE,
                        transactionCode, transaction.toString()));

                return transaction;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpStatusCode.getCode().intValue()) {

                List<Error> listErrors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpStatusCode, listErrors);

                TransactionSearchService.log.error(String.format(TransactionSearchService.SEARCH_BY_CODE,
                        transactionCode, exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpStatusCode);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            TransactionSearchService.log.error(String.format(TransactionSearchService.SEARCH_BY_CODE, transactionCode,
                    e.getMessage()));

            throw new PagSeguroServiceException(httpStatusCode, e);

        } finally {
            response.disconnect();
        }
    }

    /**
     * Search transactions associated with this set of credentials within a date range
     * 
     * @param credentials
     * @param initialDate
     * @param finalDate
     * @param page
     * @param maxPageResults
     * @throws Exception
     */
    private static TransactionSearchResult searchByDateCore(Credentials credentials, Date initialDate, Date finalDate,
            Integer page, Integer maxPageResults) throws PagSeguroServiceException {

        String dtInitial = TransactionSearchService.formatDate(initialDate);
        String dtFinal = TransactionSearchService.formatDate(finalDate);

        TransactionSearchService.log.info(String.format(TransactionSearchService.SEARCH_BY_DATE_BEGIN, dtInitial,
                dtFinal));

        ConnectionData connectionData = new ConnectionData(credentials);

        HttpConnection connection = new HttpConnection();

        TransactionSearchResult search = new TransactionSearchResult();

        HttpStatus httpStatusCode = null;

        HttpURLConnection response = connection
                .get(TransactionSearchService.buildSearchUrlByDate(connectionData, dtInitial, dtFinal, page,
                        maxPageResults), connectionData.getServiceTimeout(), connectionData.getCharset(), null);

        try {

            httpStatusCode = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpStatusCode.getCode().intValue()) {

                TransactionSearchResulParser.getHandler(response.getInputStream(), search);

                TransactionSearchService.log.info(String.format(TransactionSearchService.SEARCH_BY_DATE, dtInitial,
                        dtFinal, search.toString()));

                return search;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpStatusCode.getCode().intValue()) {

                List<Error> listErrors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpStatusCode, listErrors);

                TransactionSearchService.log.error(String.format(TransactionSearchService.SEARCH_BY_DATE, dtInitial,
                        dtFinal, exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpStatusCode);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            TransactionSearchService.log.error(String.format(TransactionSearchService.SEARCH_BY_DATE, dtInitial,
                    dtFinal, search.toString()));

            throw new PagSeguroServiceException(httpStatusCode, e);

        } finally {
            response.disconnect();
        }

    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(TransactionSearchService.DATE_FORMAT);
        return sdf.format(date);
    }
}
