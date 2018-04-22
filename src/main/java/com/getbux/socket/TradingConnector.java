package com.getbux.socket;

import com.getbux.common.TradingRequest;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import java.io.IOException;

import static com.getbux.configuration.AppConfiguration.SOCKET_CONNECTION_ATTEMPTS;
import static com.getbux.configuration.AppConfiguration.SOCKET_CONNECTION_TIMEOUT;
import static com.getbux.configuration.AppConfiguration.SUBSCRIPTION_URL;
import static com.getbux.constants.Headers.*;

public class TradingConnector {

    private static final WebSocketFactory webSocketFactory;

    static {
        webSocketFactory = new WebSocketFactory().setConnectionTimeout(SOCKET_CONNECTION_TIMEOUT);
    }

    public void connect(TradingRequest tradingRequest) {

        for(int i = 0; i < SOCKET_CONNECTION_ATTEMPTS; i++) {
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