package br.com.uol.pagseguroV2.service.checkout;

import br.com.uol.pagseguroV2.domain.AccountCredentials;
import br.com.uol.pagseguroV2.domain.checkout.Checkout;
import br.com.uol.pagseguroV2.enums.HttpStatus;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.properties.PagSeguroV2Config;
import br.com.uol.pagseguroV2.properties.PagSeguroV2System;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

public class CheckoutService {

    private CheckoutService() {
    }

    private static String convertCheckoutToJson(Checkout checkout) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(checkout);
    }

    public static String createCheckoutRequest(Checkout checkout) throws PagSeguroServiceException, IOException {
        AccountCredentials credentials = PagSeguroV2Config.getAccountCredentials();
        String url = PagSeguroV2System.getCompleteCheckoutServicePath();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String json = convertCheckoutToJson(checkout);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", "*/*")
                .addHeader("Authorization", "Bearer " + credentials.getToken())
                .addHeader("Content-type", "application/json")
                .build();

        HttpStatus httpCodeStatus = null;
        try (Response response = client.newCall(request).execute()) {
            httpCodeStatus = HttpStatus.fromCode(response.code());

            if (httpCodeStatus == null) {
                throw new PagSeguroServiceException("Connection Timeout");
            } else if (HttpURLConnection.HTTP_CREATED == httpCodeStatus.getCode()) {

                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode linksNode = rootNode.path("links");

                for (JsonNode linkNode : linksNode) {
                    if ("PAY".equals(linkNode.path("rel").asText())) {
                        return linkNode.path("href").asText();
                    }
                }

                throw new PagSeguroServiceException("PAY link not found in the response.");
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }
        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PagSeguroServiceException(httpCodeStatus, e);
        }
    }
}
