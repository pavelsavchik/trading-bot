package com.getbux.service;

import com.getbux.api.TradingAPIClient;
import com.getbux.socket.TradingQuote;
import com.getbux.common.TradingRequest;
import com.getbux.socket.TradingResult;
import java.io.IOException;

public class TradingService {

    public TradingResult process(TradingQuote tradingQuote, TradingRequest tradingRequest, TradingResult lastTradingResult)
            throws IOException {
        TradingResult tradingResult = new TradingResult(lastTradingResult);
        Double currentPrice = tradingQuote.getCurrentPrice();

        if (!lastTradingResult.isBought()) {
            //TODO: replace equals by comparision with precision
            if (currentPrice.equals(tradingRequest.getBuyPrice())) {
                String positionId = TradingAPIClient.buy(tradingRequest);
                tradingResult.setPositionId(positionId);
            }
        } else {
            if (currentPrice >= tradingRequest.getUpperLimitSellPrice() || currentPrice <= tradingRequest.getLowerLimitSellPrice()) {
                TradingAPIClient.sell(tradingRequest);
            }
        }

        return tradingResult;
    }

}
