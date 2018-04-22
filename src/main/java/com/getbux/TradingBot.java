package com.getbux;

import com.getbux.common.TradingRequest;
import com.getbux.service.TradingService;
import com.getbux.socket.TradingQuoteListener;
import com.getbux.socket.TradingConnector;
import com.getbux.ui.TradingRequestReader;
import com.neovisionaries.ws.client.WebSocketListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TradingBot {

    private TradingRequestReader tradingRequestReader;

    private TradingConnector tradingConnector;

    private final TradingService tradingService;

    @Autowired
    public TradingBot(TradingRequestReader tradingRequestReader, TradingConnector tradingConnector, TradingService tradingService) {
        this.tradingRequestReader = tradingRequestReader;
        this.tradingConnector = tradingConnector;
        this.tradingService = tradingService;
    }

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext("com.getbux");
        TradingBot tradingBot = context.getBean(TradingBot.class);
        tradingBot.run();
    }

    public void run() {
        TradingRequest tradingRequest = tradingRequestReader.read();
        WebSocketListener listener = new TradingQuoteListener(tradingRequest, tradingService);
        tradingConnector.connect(listener);
    }

}