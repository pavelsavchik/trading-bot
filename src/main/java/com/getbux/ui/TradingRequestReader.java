package com.getbux.ui;

import com.getbux.common.TradingRequest;
import org.springframework.stereotype.Component;

@Component
public class TradingRequestReader {

    public TradingRequest read() {

        TradingRequest tradingRequest = new TradingRequest();
        ConsoleReader consoleReader = new ConsoleReader();

        System.out.println("Please specify product id:");
        tradingRequest.setProductId(consoleReader.readString());

        System.out.println("Please specify buy price:");
        tradingRequest.setBuyPrice(
                consoleReader.readDouble(0.)
        );

        System.out.println("Please specify upper limit sell price:");
        tradingRequest.setUpperLimitSellPrice(
                consoleReader.readDouble(tradingRequest.getBuyPrice())
        );

        System.out.println("Please specify lower limit sell price:");
        tradingRequest.setLowerLimitSellPrice(
                consoleReader.readDouble(0., tradingRequest.getBuyPrice())
        );

        System.out.println("I got you! Now please wait");

        return tradingRequest;
    }

}
