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

package br.com.uol.pagseguro.service.authorization;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.domain.authorization.AuthorizationRequest;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.authorization.AuthorizationRequestParser;
import br.com.uol.pagseguro.service.ConnectionData;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

/**
 * 
 * Class AuthorizationService
 */
public class AuthorizationService {
	
	/**
     * @var Log
     */
    private static Log log = new Log(AuthorizationService.class);

    /**
     * 
     * @param ConnectionData
     *            connectionData
     * @return string
     * @throws PagSeguroServiceException
     */
    public static String buildAuthorizationRequestUrl(ConnectionData connectionData) throws PagSeguroServiceException {
        return connectionData.getWSAuthorizationUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    /**
     * Build authorization url
     * 
     * @param connection
     * @param code
     * @return string
     */
    private static String buildAuthorizationUrl(ConnectionData connection, String code) {
        return connection.getAuthorizationUrl() + "?code=" + code;
    }

    /**
     * 
     * @param credentials
     * @param authorization
     * @param onlyCheckoutCode
     * @return string
     * @throws Exception
     */
    public static String createAuthorizationRequest(Credentials credentials, AuthorizationRequest authorization, Boolean onlyCheckoutCode)
            throws PagSeguroServiceException {

        AuthorizationService.log.info(String.format("AuthorizationService.Register( %s ) - begin", authorization.toString()));

        ConnectionData connectionData = new ConnectionData(credentials);

        Map<Object, Object> data = AuthorizationRequestParser.getData(authorization);
        
        String url = AuthorizationService.buildAuthorizationRequestUrl(connectionData);

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.post(url, data, connectionData.getServiceTimeout(),
                connectionData.getCharset(), null);

        try {

            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());
            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {

                String authorizationReturn = null;
                String code = AuthorizationRequestParser.readSuccessXml(response);

                if (onlyCheckoutCode) {
                	authorizationReturn = code;
                } else {
                	authorizationReturn = AuthorizationService.buildAuthorizationUrl(connectionData, code);
                }

                AuthorizationService.log.info(String.format("AuthorizationService.Register( %1s ) - end  %2s )",
                        authorization.toString(), code));

                return authorizationReturn;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {

                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                AuthorizationService.log.error(String.format("AuthorizationService.Register( %1s ) - error %2s",
                        authorization.toString(), exception.getMessage()));

                throw exception;

            } else {

                throw new PagSeguroServiceException(httpCodeStatus);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

        	AuthorizationService.log.error(String.format("AuthorizationService.Register( %1s ) - error %2s", authorization.toString(),
                    e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);

        } finally {
            response.disconnect();
        }
    }

}
