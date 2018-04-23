package com.getbux.service;

import com.getbux.api.TradingAPIClient;
import com.getbux.socket.TradingQuote;
import com.getbux.common.TradingRequest;
import com.getbux.socket.TradingResult;
import com.getbux.utils.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TradingService {

    private TradingAPIClient apiClient;

    @Autowired
    public TradingService(TradingAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public TradingResult process(TradingQuote tradingQuote, TradingRequest tradingRequest, TradingResult lastTradingResult)
            throws IOException {

        if(tradingQuote == null || tradingRequest == null) {
            return null;
        }

        TradingResult tradingResult = new TradingResult(lastTradingResult);
        Double currentPrice = tradingQuote.getCurrentPrice();

        if (tradingResult.getPositionId() == null) {
            if (PriceUtils.equals(currentPrice, tradingRequest.getBuyPrice())) {
                String positionId = apiClient.buy(tradingRequest.getProductId());
                tradingResult.setPositionId(positionId);
            }
        } else {
            if (!PriceUtils.inRange(currentPrice,
                    tradingRequest.getLowerLimitSellPrice(),
                    tradingRequest.getUpperLimitSellPrice())) {
                apiClient.sell(tradingResult.getPositionId());
                tradingResult.setSold(true);
            }
        }

        return tradingResult;
    }

}
