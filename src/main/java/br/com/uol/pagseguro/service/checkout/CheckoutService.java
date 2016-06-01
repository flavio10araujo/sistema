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

package br.com.uol.pagseguro.service.checkout;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.checkout.CheckoutParser;
import br.com.uol.pagseguro.service.ConnectionData;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * 
 * Class CheckoutRequestService
 */
public class CheckoutService {

    private CheckoutService() {
    }

    /**
     * @var Log
     */
    private static Log log = new Log(CheckoutService.class);

    /**
     * 
     * @param ConnectionData
     *            connectionData
     * @return string
     * @throws PagSeguroServiceException
     */
    public static String buildCheckoutRequestUrl(ConnectionData connectionData) throws PagSeguroServiceException {
        return connectionData.getWebServiceUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    /**
     * Build checkout url
     * 
     * @param connection
     * @param code
     * @return string
     */
    private static String buildCheckoutUrl(ConnectionData connection, String code) {
        return connection.getCheckoutUrl() + "?code=" + code;
    }

    /**
     * 
     * @param credentials
     * @param checkout
     * @param onlyCheckoutCode
     * @return string
     * @throws Exception
     */
    public static String createCheckoutRequest(Credentials credentials, Checkout checkout, Boolean onlyCheckoutCode)
            throws PagSeguroServiceException {

        CheckoutService.log.info(String.format("CheckoutService.Register( %s ) - begin", checkout.toString()));

        ConnectionData connectionData = new ConnectionData(credentials);

        Map<Object, Object> data = CheckoutParser.getData(checkout);

        String url = CheckoutService.buildCheckoutRequestUrl(connectionData);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.post(url, data, connectionData.getServiceTimeout(),
                connectionData.getCharset(), null);

        try {

            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());
            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {

                String checkoutReturn = null;
                String code = CheckoutParser.readSuccessXml(response);

                if (onlyCheckoutCode) {
                    checkoutReturn = code;
                } else {
                    checkoutReturn = CheckoutService.buildCheckoutUrl(connectionData, code);
                }

                CheckoutService.log.info(String.format("CheckoutService.Register( %1s ) - end  %2s )",
                        checkout.toString(), code));

                return checkoutReturn;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {

                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                CheckoutService.log.error(String.format("CheckoutService.Register( %1s ) - error %2s",
                        checkout.toString(), exception.getMessage()));

                throw exception;

            } else {

                throw new PagSeguroServiceException(httpCodeStatus);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

            CheckoutService.log.error(String.format("CheckoutService.Register( %1s ) - error %2s", checkout.toString(),
                    e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);

        } finally {
            response.disconnect();
        }
    }

}
