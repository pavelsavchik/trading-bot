package com.getbux.socket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;

import static com.getbux.configuration.AppConfiguration.*;

public class TradingConnector {

    private final WebSocketFactory webSocketFactory;

    public TradingConnector() {
        this.webSocketFactory = new WebSocketFactory().setConnectionTimeout(SOCKET_CONNECTION_TIMEOUT);
    }

    public TradingConnector(WebSocketFactory webSocketFactory) {
        this.webSocketFactory = webSocketFactory;
    }

    public void connect(WebSocketListener listener) {
        for(int i = 0; i < SOCKET_CONNECTION_ATTEMPTS; i++) {
            try {
                WebSocket socket = initializeSocket();
                socket.addListener(listener);
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