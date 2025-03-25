package br.com.uol.pagseguroV2.service.checkout;

import br.com.uol.pagseguroV2.domain.Credentials;
import br.com.uol.pagseguroV2.domain.Error;
import br.com.uol.pagseguroV2.domain.checkout.Checkout;
import br.com.uol.pagseguroV2.enums.HttpStatus;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.parser.checkout.CheckoutParser;
import br.com.uol.pagseguroV2.service.ConnectionData;
import br.com.uol.pagseguroV2.utils.HttpConnection;
import br.com.uol.pagseguroV2.xmlparser.ErrorsParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class CheckoutService {

    private CheckoutService() {
    }

    public static String buildCheckoutRequestUrl(ConnectionData connectionData) throws PagSeguroServiceException {
        return connectionData.getWebServiceUrl() + "?" + connectionData.getCredentialsUrlQuery();
    }

    private static String buildCheckoutUrl(ConnectionData connection, String code) {
        return connection.getCheckoutUrl() + "?code=" + code;
    }

    private static String convertCheckoutToJson(Checkout checkout) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(checkout);
    }

    public static String createCheckoutRequest(Credentials credentials, Checkout checkout) throws PagSeguroServiceException, IOException {
        ConnectionData connectionData = new ConnectionData(credentials);
        String url = CheckoutService.buildCheckoutRequestUrl(connectionData);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String json = convertCheckoutToJson(checkout);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", "*/*")
                .addHeader("Authorization", "Bearer 6ABB191E69EF4CD29E15E3D0B8CAE453")
                .addHeader("Content-type", "application/json")
                .build();

        HttpStatus httpCodeStatus = null;
        try (Response response = client.newCall(request).execute()) {
            httpCodeStatus = HttpStatus.fromCode(response.code());

            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode()) {
                String code = CheckoutParser.readSuccessXml(response);
                return CheckoutService.buildCheckoutUrl(connectionData, code);
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }
        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PagSeguroServiceException(httpCodeStatus, e);
        }
    }

    public static String createCheckoutRequest(Credentials credentials, Checkout checkout, Boolean onlyCheckoutCode) throws PagSeguroServiceException {
        ConnectionData connectionData = new ConnectionData(credentials);
        Map<Object, Object> data = CheckoutParser.getData(checkout);
        String url = CheckoutService.buildCheckoutRequestUrl(connectionData);
        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;
        HttpURLConnection response = connection.post(url, data, connectionData.getServiceTimeout(), connectionData.getCharset(), null);

        try {
            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());
            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode()) {
                String checkoutReturn;
                String code = CheckoutParser.readSuccessXml(response);

                if (Boolean.TRUE.equals(onlyCheckoutCode)) {
                    checkoutReturn = code;
                } else {
                    checkoutReturn = CheckoutService.buildCheckoutUrl(connectionData, code);
                }

                return checkoutReturn;
            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode()) {
                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());
                throw new PagSeguroServiceException(httpCodeStatus, errors);
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }
        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PagSeguroServiceException(httpCodeStatus, e);
        } finally {
            response.disconnect();
        }
    }
}
