package com.getbux.service;

import com.getbux.api.TradingAPIClient;
import com.getbux.socket.TradingQuote;
import com.getbux.common.TradingRequest;
import com.getbux.socket.TradingResult;
import com.getbux.utils.PriceUtils;

import java.io.IOException;

public class TradingService {

    public TradingResult process(TradingQuote tradingQuote, TradingRequest tradingRequest, TradingResult lastTradingResult)
            throws IOException {
        TradingResult tradingResult = new TradingResult(lastTradingResult);
        Double currentPrice = tradingQuote.getCurrentPrice();

        if (!tradingResult.isBought()) {
            if (PriceUtils.equals(currentPrice, tradingRequest.getBuyPrice())) {
                String positionId = TradingAPIClient.buy(tradingRequest);
                tradingResult.setPositionId(positionId);
            }
        } else {
            if (PriceUtils.inRange(currentPrice,
                    tradingRequest.getLowerLimitSellPrice(),
                    tradingRequest.getUpperLimitSellPrice())) {
                TradingAPIClient.sell(tradingRequest);
            }
        }

        return tradingResult;
    }

}
