package com.getbux.service;

import com.getbux.common.TradingRequest;
import com.getbux.socket.TradingQuote;
import com.getbux.socket.TradingResult;

public interface TradingService {

    TradingResult process(TradingQuote tradingQuote,
                          TradingRequest tradingRequest,
                          TradingResult lastTradingResult);

}
