package com.getbux;

import com.getbux.common.TradingRequest;
import com.getbux.socket.ProductUpdateListener;
import com.getbux.socket.TradingConnector;
import com.getbux.ui.TradingRequestReader;
import com.neovisionaries.ws.client.WebSocketListener;

public class TradingBot {

    public static void main(String[] args) {
        TradingRequest tradingRequest = new TradingRequestReader().read();
        WebSocketListener listener = new ProductUpdateListener(tradingRequest);
        new TradingConnector().connect(listener);
    }

}