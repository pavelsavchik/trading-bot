package com.getbux;

import com.getbux.common.TradingRequest;
import com.getbux.socket.TradingConnector;
import com.getbux.ui.TradingRequestReader;

public class TradingBot {

    private static final TradingRequestReader tradingRequestReader;

    static {
        tradingRequestReader = new TradingRequestReader();
    }

    public static void main(String[] args) {
        TradingRequest tradingRequest = tradingRequestReader.read();
        new TradingConnector().connect(tradingRequest);
    }

}