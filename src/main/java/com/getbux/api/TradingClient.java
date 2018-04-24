package com.getbux.api;

public interface TradingClient {

    String buy(String productId);

    Boolean sell(String positionId);

}
