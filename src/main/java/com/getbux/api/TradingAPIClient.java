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
public class TradingAPIClient implements TradingClient {

    private final CloseableHttpClient httpClient;

    @Autowired
    public TradingAPIClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String buy(String productId) {
        if(productId == null) {
            return null;
        }

        String positionId;

        try {
            BuyRequest buyRequest = new DefaultBuyRequest(productId);
            String body = mapper.writeValueAsString(buyRequest);
            HttpPost request = new HttpPost(BUY_URL);
            request.setEntity(new StringEntity(body));
            HttpResponse response = executeWithHeaders(request);
            positionId = getPositionId(response);
        } catch (IOException exception) {
            positionId = null;
        }

        System.out.println(positionId != null ? "Product is bought" : "Product buying failed");
        return positionId;
    }

    public Boolean sell(String positionId) {
        if(positionId == null) {
            return false;
        }

        String profitAndLossAmount;
        HttpDelete request = new HttpDelete(SELL_URL + positionId);

        try {
            HttpResponse response = executeWithHeaders(request);
            profitAndLossAmount = getProfitAndLossAmount(response);
        } catch (IOException exception) {
            profitAndLossAmount = null;
        }

        System.out.println(profitAndLossAmount != null ?
                "Product is sold with profit/loss " + profitAndLossAmount :
                "Product selling failed");

        return profitAndLossAmount != null;
    }

    private String getProfitAndLossAmount(HttpResponse response) throws IOException {
        if(response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            SellResponse sellResponse = mapper.readValue(EntityUtils.toString(response.getEntity()), SellResponse.class);
            return sellResponse.getProfitAndLossAmount();
        }
        return null;
    }

    private String getPositionId(HttpResponse response) throws IOException {
        if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String responseBody = EntityUtils.toString(response.getEntity());
            return mapper.readValue(responseBody, BuyResponse.class).getPositionId();
        }
        return null;
    }

    private HttpResponse executeWithHeaders(HttpUriRequest request) throws IOException {
        request.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
        request.addHeader(LANGUAGE_HEADER_NAME, LANGUAGE_HEADER_VALUE);
        request.addHeader(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE);
        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);

        return httpClient.execute(request);
    }

}
