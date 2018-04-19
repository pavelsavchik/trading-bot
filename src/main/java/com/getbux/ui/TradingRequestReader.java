package com.getbux.ui;

import com.getbux.common.TradingRequest;

public class TradingRequestReader {

    public TradingRequest read() {
        TradingRequest tradingRequest = new TradingRequest();
        tradingRequest.setProductId("sb26493");
        tradingRequest.setBuyPrice(12535.);
        tradingRequest.setLowerLimitSellPrice(12534.5);
        tradingRequest.setUpperLimitSellPrice(12536.9);
        return tradingRequest;
    }

}
