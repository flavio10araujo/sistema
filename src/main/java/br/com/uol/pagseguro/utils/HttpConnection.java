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

package br.com.uol.pagseguro.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.helper.PagSeguroUtil;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.properties.PagSeguroSystem;

/**
 * 
 * Class HttpConnection
 */
public class HttpConnection {

    /**
     * @var Log
     */
    private final Log log = new Log(HttpConnection.class);

    /**
     * POST
     * 
     * @param urlPS
     * @param data
     * @param timeout
     * @param charset
     * @return connection
     * @throws PagSeguroServiceException
     * 
     * @deprecated use {@link #post(String, Map, String, String, String)} instead.
     */
    @Deprecated
    public HttpURLConnection post(String urlPS, Map<Object, Object> data, String timeout, String charset)
            throws PagSeguroServiceException {

        return post(urlPS, data, timeout, charset, null);
    }

    /**
     * POST
     * 
     * @param urlPS
     * @param data
     * @param timeout
     * @param charset
     * @return connection
     * @throws PagSeguroServiceException
     */
    public HttpURLConnection post(String urlPS, Map<Object, Object> data, String timeout, String charset,
            String acceptHeader) throws PagSeguroServiceException {

        HttpURLConnection connection = getConnection(urlPS, timeout, charset, "POST", acceptHeader);

        try {
            // Send POST data
            OutputStream out = connection.getOutputStream();
            Writer write = new OutputStreamWriter(out, charset);

            write.write(PagSeguroUtil.urlQuery(data));

            write.close();
            out.close();

            return connection;
        } catch (IOException e) {
            log.error("Error when trying execute method connection: " + e.getMessage());
            throw new PagSeguroServiceException("Error when trying write or set request method", e);
        }
    }

    /**
     * Http request method without data
     * 
     * @param urlPS
     * @param timeout
     * @param charset
     * @param method
     * @return
     * @throws PagSeguroServiceException
     */
    @Deprecated
    public HttpURLConnection httpRequestMethod(String urlPS, String timeout, String charset, String method)
            throws PagSeguroServiceException {
        return httpRequestMethod(urlPS, timeout, charset, method, null);
    }

    /**
     * Http request method without data
     * 
     * @param urlPS
     * @param timeout
     * @param charset
     * @param method
     * @return
     * @throws PagSeguroServiceException
     */
    public HttpURLConnection httpRequestMethod(String urlPS, String timeout, String charset, String method,
            String acceptHeader) throws PagSeguroServiceException {

        return getConnection(urlPS, timeout, charset, method, acceptHeader);
    }

    /**
     * GET
     * 
     * @param urlPS
     * @param timeout
     * @param charset
     * @return connection
     * @throws PagSeguroServiceException
     * 
     * @deprecated use {@link #get(String, String, String, String, String)} instead.
     */
    @Deprecated
    public HttpURLConnection get(String urlPS, String timeout, String charset) throws PagSeguroServiceException {

        return get(urlPS, timeout, charset, null);
    }

    /**
     * GET
     * 
     * @param urlPS
     * @param timeout
     * @param charset
     * @return connection
     * @throws PagSeguroServiceException
     */
    public HttpURLConnection get(String urlPS, String timeout, String charset, String acceptHeader)
            throws PagSeguroServiceException {

        return getConnection(urlPS, timeout, charset, "GET", acceptHeader);

    }

    /**
     * Generates a Connection
     * 
     * @param urlPS
     * @param timeout
     * @param charset
     * @param method
     * @return connection
     * @throws PagSeguroServiceException
     */
    private HttpURLConnection getConnection(String urlPS, String timeout, String charset, String method,
            String acceptHeader) throws PagSeguroServiceException {
        URL url = null;
        HttpURLConnection connection = null;

        try {

            // Creates a connection
            url = new URL(urlPS);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("charset", charset);
            connection.setRequestProperty("Content-type", PagSeguroSystem.getContentTypeFormUrlEncoded());

            if (acceptHeader != null) {
                connection.setRequestProperty("Accept", acceptHeader);
            }

            connection.setRequestProperty("lib-description", "java:" + PagSeguroSystem.getLibversion());
            connection.setRequestProperty("language-engine-description",
                    "java:" + PagSeguroSystem.getLanguageEnginedescription());

            String moduleVersion = PagSeguroConfig.getModuleVersion();
            if (moduleVersion != null) {
                connection.setRequestProperty("module-description", moduleVersion);
            }

            String cmsVersion = PagSeguroConfig.getCmsVersion();
            if (cmsVersion != null) {
                connection.setRequestProperty("cms-description", cmsVersion);
            }

            return connection;

        } catch (MalformedURLException e) {
            log.error("Error when trying execute method connection: " + e.getMessage());
            throw new PagSeguroServiceException("Error when trying create Url object", e);
        } catch (IOException e) {
            log.error("Error when trying execute method connection: " + e.getMessage());
            throw new PagSeguroServiceException("Error when trying write or set request method", e);
        } catch (Exception e) {
            log.error("Error when trying execute method connection: " + e.getMessage());
            throw new PagSeguroServiceException("Generic error", e);
        }
    }
}
