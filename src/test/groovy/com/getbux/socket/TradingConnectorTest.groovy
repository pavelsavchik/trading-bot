package com.getbux.socket

import com.getbux.configuration.AppConfiguration
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketListener
import spock.lang.Specification

class TradingConnectorTest extends Specification {

    def webSocketFactory = Mock(WebSocketFactory)

    def listener = Mock(WebSocketListener)

    def webSocket = Mock(WebSocket)

    def tradingConnector = new TradingConnector(webSocketFactory)

    def "test regular case"() {

        when:
        tradingConnector.connect(listener)

        then:
        1 * webSocketFactory.createSocket(AppConfiguration.SUBSCRIPTION_URL) >> webSocket
        1 * webSocket.addListener(listener)
        1 * webSocket.connect()
    }

    def "test connection attempts for WebSocketException"() {
        when:
        tradingConnector.connect(listener)

        then:
        AppConfiguration.SOCKET_CONNECTION_ATTEMPTS * webSocketFactory.createSocket(AppConfiguration.SUBSCRIPTION_URL) >> webSocket
        AppConfiguration.SOCKET_CONNECTION_ATTEMPTS * webSocket.addListener(listener)
        AppConfiguration.SOCKET_CONNECTION_ATTEMPTS * webSocket.connect() >> {throw Mock(WebSocketException)}
    }

    def "test connection attempts for IOException"() {
        when:
        tradingConnector.connect(listener)

        then:
        AppConfiguration.SOCKET_CONNECTION_ATTEMPTS * webSocketFactory.createSocket(AppConfiguration.SUBSCRIPTION_URL) >> webSocket
        AppConfiguration.SOCKET_CONNECTION_ATTEMPTS * webSocket.addListener(listener)
        AppConfiguration.SOCKET_CONNECTION_ATTEMPTS * webSocket.connect() >> {throw Mock(IOException)}
    }

}