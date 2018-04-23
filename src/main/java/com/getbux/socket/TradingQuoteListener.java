package com.getbux.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.getbux.common.TradingRequest;
import com.getbux.service.TradingService;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;

import static com.getbux.utils.JSONUtils.mapper;

public class TradingQuoteListener extends WebSocketAdapter {

    private final TradingService tradingService;

    private Boolean isConnected = false;

    private TradingResult lastTradingResult;

    private final TradingRequest tradingRequest;

    public TradingQuoteListener(TradingRequest tradingRequest, TradingService tradingService) {
        this.tradingRequest = tradingRequest;
        this.tradingService = tradingService;
    }

    @Override
    public void onTextMessage(WebSocket socket, String message) throws WebSocketException, IOException {

        EventMessage eventMessage = parseEventMessage(message);

        if(eventMessage == null) {
            return;
        }

        switch (eventMessage.getType()) {
            case "connect.connected":
                processConnectMessage();
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

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        System.out.println("An error occurred: " + cause.getMessage());
        websocket.disconnect();
        System.out.println("Disconnected");
    }

    private void processConnectMessage() throws JsonProcessingException {
        isConnected = true;
        System.out.println("Connected successfully, waiting for quotes");
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
                System.out.println("Failed trading quote message processing, message is ignored");
                return;
            }
            System.out.println("Current price is " + tradingQuote.getCurrentPrice());
            lastTradingResult = tradingService.process(tradingQuote, tradingRequest, lastTradingResult);
            if (lastTradingResult.isSold()) {
                socket.disconnect();
            }
        }
    }

    private EventMessage parseEventMessage(String message) {
        try {
            return mapper.readValue(message, EventMessage.class);
        } catch (IOException exception) {
            System.out.println("Can't parse message, ignore it");
            return null;
        }

    }
}
