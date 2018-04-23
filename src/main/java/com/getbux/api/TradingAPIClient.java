package com.getbux.api;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.getbux.configuration.AppConfiguration.*;
import static com.getbux.utils.JSONUtils.mapper;

@Component
public class TradingAPIClient {

    private final CloseableHttpClient httpClient;

    @Autowired
    public TradingAPIClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String buy(String productId) throws IOException {
        if(productId == null) {
            return null;
        }

        String url = API_URL + BUY_PATH;
        BuyRequest buyRequest = new DefaultBuyRequest(productId);
        String body = mapper.writeValueAsString(buyRequest);
        HttpPost request = new HttpPost(url);

        request.setEntity(new StringEntity(body));
        HttpResponse response = executeWithHeaders(request);

        if(response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            System.out.println("Product is bought");
            String responseBody = EntityUtils.toString(response.getEntity());
            return mapper.readValue(responseBody, BuyResponse.class).getPositionId();
        } else {
            System.out.println("Product buying failed");
            return null;
        }
    }

    public Boolean sell(String positionId) throws IOException {
        if(positionId == null) {
            return false;
        }

        String url = API_URL + SELL_PATH + positionId;
        HttpDelete request = new HttpDelete(url);

        HttpResponse response = executeWithHeaders(request);
        if(response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            System.out.println("Product is sold");
            return true;
        } else {
            System.out.println("Product selling failed");
            return false;
        }

    }

    private HttpResponse executeWithHeaders(HttpUriRequest request) throws IOException {
        request.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
        request.addHeader(LANGUAGE_HEADER_NAME, LANGUAGE_HEADER_VALUE);
        request.addHeader(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE);
        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);

        return httpClient.execute(request);
    }

}
