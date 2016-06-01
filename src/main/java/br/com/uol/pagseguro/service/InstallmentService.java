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

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.installment.Installments;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.InstallmentsParser;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * 
 * Class Payment Service
 */
public class InstallmentService {

    /**
     * @var Log
     */
    private static Log log = new Log(InstallmentService.class);

    private static String buildInstallmentsRequestUrl(ConnectionData connectionData) //
            throws PagSeguroServiceException {
        return connectionData.getInstallmentsUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    public static Installments getInstallments(Credentials credentials, //
            BigDecimal amount) //
            throws PagSeguroServiceException {
        return getInstallments(credentials, amount, null);
    }

    public static Installments getInstallments(Credentials credentials, //
            BigDecimal amount, //
            String cardBrand) //
            throws PagSeguroServiceException {
        log.info(String.format("InstallmentService.getInstallments(%1s, %2s) - begin", //
                amount, //
                cardBrand));

        ConnectionData connectionData = new ConnectionData(credentials);

        StringBuilder url = new StringBuilder(InstallmentService.buildInstallmentsRequestUrl(connectionData));
        url.append("&amount=" + amount.toString());
        if (cardBrand != null) {
            url.append("&cardBrand=" + cardBrand);
        }

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.get(url.toString(), //
                connectionData.getServiceTimeout(), //
                connectionData.getCharset(),
                null);

        try {
            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());

            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {
                Installments installments = InstallmentsParser.readInstallments(response.getInputStream());

                log.info("InstallmentService.getInstallments() - end");

                return installments;
            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {
                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                log.error(String.format("InstallmentService.getInstallments() - error %s", //
                        exception.getMessage()));

                throw exception;
            } else if (HttpURLConnection.HTTP_UNAUTHORIZED == httpCodeStatus.getCode().intValue()) {
                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus);

                log.error(String.format("InstallmentService.getInstallments() - error %s", //
                        exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }
        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(String.format("PaymentService.getInstallments() - error %s", //
                    e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);
        } finally {
            response.disconnect();
        }
    }

}
