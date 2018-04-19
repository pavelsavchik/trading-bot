package com.getbux.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.getbux.service.TradingService;
import com.getbux.common.TradingRequest;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import java.io.IOException;
import static com.getbux.utils.JSONUtils.mapper;

public class ProductUpdateListener extends WebSocketAdapter {

    private final TradingService tradingService;

    private Boolean isConnected = false;

    private TradingResult lastTradingResult;

    private TradingRequest tradingRequest;

    ProductUpdateListener(TradingRequest tradingRequest) {
        this.tradingRequest = tradingRequest;
        this.tradingService = new TradingService();
    }

    ProductUpdateListener(TradingRequest tradingRequest, TradingService tradingService) {
        this.tradingRequest = tradingRequest;
        this.tradingService = tradingService;
    }

    @Override
    public void onTextMessage(WebSocket socket, String message) throws WebSocketException, IOException {

        EventMessage eventMessage;

        try {
            eventMessage = mapper.readValue(message, EventMessage.class);
        } catch (IOException exception) {
            return;
        }

        switch (eventMessage.getType()) {
            case "connect.connected":
                processConnectMessage(socket);
                subscribeToProductCode(socket);
                break;
            case "connect.failed":
                System.out.println("Connect failed: " + eventMessage.getBody().get("developerMessage"));
                socket.disconnect();
                break;
            case "trading.quote":
                processQuoteMessage(socket, eventMessage);
                break;
            default:
                break;
        }
    }

    private void processConnectMessage(WebSocket socket) throws JsonProcessingException {
        isConnected = true;
        System.out.println("Connected successfully!");
    }

    private void subscribeToProductCode(WebSocket socket) throws JsonProcessingException {
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest(tradingRequest.getProductId());
        String subscriptionRequestJSON;
        try {
            subscriptionRequestJSON = mapper.writeValueAsString(subscriptionRequest);
        } catch (JsonProcessingException exception) {
            System.out.println("Can't create subscription message");
            throw exception;
        }
        socket.sendText(subscriptionRequestJSON);
    }

    private void processQuoteMessage(WebSocket socket, EventMessage eventMessage) throws IOException {
        if (isConnected) {
            TradingQuote tradingQuote;
            try {
                tradingQuote = mapper.convertValue(eventMessage.getBody(), TradingQuote.class);
            } catch (IllegalArgumentException exception) {
                System.out.println("Failed trading quote message processing, message is ignored.");
                return;
            }
            lastTradingResult = tradingService.process(tradingQuote, tradingRequest, lastTradingResult);
            if (lastTradingResult.isSold()) {
                socket.disconnect();
            }
        }
    }
}
