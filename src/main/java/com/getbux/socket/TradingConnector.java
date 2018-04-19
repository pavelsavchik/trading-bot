package com.getbux.socket;

import com.getbux.common.TradingRequest;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import java.io.IOException;
import static com.getbux.constants.Headers.*;

public class TradingConnector {

    private static final String SUBSCRIPTION_URL = "https://rtf.beta.getbux.com/subscriptions/me";

    private static final Integer SOCKET_CONNECTION_TIMEOUT = 5000;

    private static final WebSocketFactory webSocketFactory;

    private static final Integer ATTEMPTS = 3;

    static {
        webSocketFactory = new WebSocketFactory().setConnectionTimeout(SOCKET_CONNECTION_TIMEOUT);
    }

    public void connect(TradingRequest tradingRequest) {

        for(int i = 0; i < ATTEMPTS; i++) {
            try {
                WebSocket socket = initializeSocket();
                socket.addListener(new ProductUpdateListener(tradingRequest));
                socket.connect();
            } catch (IOException | WebSocketException exception) {
                System.out.println("Exception: " + exception.getMessage());
                continue;
            }

            break;
        }
    }

    private WebSocket initializeSocket() throws IOException {
        WebSocket ws = webSocketFactory.createSocket(SUBSCRIPTION_URL);
        ws.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
        ws.addHeader(LANGUAGE_HEADER_NAME, LANGUAGE_HEADER_VALUE);

        return ws;
    }

}