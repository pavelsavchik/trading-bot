package com.getbux.service;

import com.getbux.api.TradingClient;
import com.getbux.socket.TradingQuote;
import com.getbux.common.TradingRequest;
import com.getbux.socket.TradingResult;
import com.getbux.utils.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceBasedTradingService implements TradingService {

    private TradingClient tradingClient;

    @Autowired
    public PriceBasedTradingService(TradingClient tradingClient) {
        this.tradingClient = tradingClient;
    }

    @Override
    public TradingResult process(TradingQuote tradingQuote, TradingRequest tradingRequest, TradingResult lastTradingResult) {


        if(tradingQuote == null || tradingRequest == null) {
            return null;
        }

        TradingResult tradingResult = new TradingResult(lastTradingResult);
        Double currentPrice = tradingQuote.getCurrentPrice();

        if (tradingResult.getPositionId() == null) {
            if (PriceUtils.equals(currentPrice, tradingRequest.getBuyPrice())) {
                String positionId = tradingClient.buy(tradingRequest.getProductId());
                tradingResult.setPositionId(positionId);
            }
        } else {
            if (!PriceUtils.inRange(currentPrice,
                    tradingRequest.getLowerLimitSellPrice(),
                    tradingRequest.getUpperLimitSellPrice())) {
                Boolean isSold = tradingClient.sell(tradingResult.getPositionId());
                tradingResult.setSold(isSold);
            }
        }

        return tradingResult;
    }

}
