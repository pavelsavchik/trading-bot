package com.getbux.socket

import com.getbux.common.Messages
import com.getbux.service.TradingService
import com.getbux.common.TradingRequest
import com.neovisionaries.ws.client.WebSocket
import org.skyscreamer.jsonassert.JSONAssert
import spock.lang.Specification

class TradingQuoteListenerTest extends Specification {

    def tradingService = Mock(TradingService)
    
    final PRODUCT_ID = "someId"

    def tradingRequest = new TradingRequest(
            productId: PRODUCT_ID,
            buyPrice: 2.52,
            lowerLimitSellPrice: 2.45,
            upperLimitSellPrice: 2.70
    )

    def productUpdateListener

    def setup() {
        productUpdateListener = new TradingQuoteListener(tradingRequest, tradingService)
    }

    def "test failed connection"() {
        def webSocket = Mock(WebSocket)

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getConnectFailedMessage())

        then:
        1 * webSocket.disconnect()
    }

    def "test successful connection"() {
        def webSocket = Mock(WebSocket)

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getConnectMessage())

        then:
        1 * webSocket.sendText(_) >> { String message ->
            JSONAssert.assertEquals(message, Messages.getSubscriptionMessage(PRODUCT_ID), true)
        }
    }

    def "test trading quote message processing when isn't connected yet"() {
        def webSocket = Mock(WebSocket)

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID))

        then:
        //Message should be ignored
        0 * tradingService.process(* _)
    }


    def "test trading quote message processing"() {
        def webSocket = Mock(WebSocket)

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getConnectMessage())
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID))

        then:
        1 * tradingService.process(_, tradingRequest, null) >> new TradingResult()
    }

    def "test multiple trading quote messages processing"() {
        def webSocket = Mock(WebSocket)
        def tradingResult = new TradingResult(positionId: "pos")

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getConnectMessage())
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID))
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID))

        then:
        1 * tradingService.process(_, tradingRequest, null) >> tradingResult
        1 * tradingService.process(_, tradingRequest, tradingResult) >> tradingResult
    }

    def "test trading quote message processing when item was sold"() {
        def webSocket = Mock(WebSocket)

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getConnectMessage())
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID))

        then:
        1 * tradingService.process(_, tradingRequest, null) >> new TradingResult(isSold: true)
        1 * webSocket.disconnect()
    }

    def "test full trading quote messages flow"() {
        def webSocket = Mock(WebSocket)
        def tradingResult = new TradingResult(positionId: "pos")

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getConnectMessage())
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID,"2.52"))

        then:
        1 * tradingService.process(_, tradingRequest, null) >> { TradingQuote tradingQuote, request, result ->
            assert tradingQuote.currentPrice == 2.52
            return tradingResult
        }
        0 * webSocket.disconnect()

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID,"2.54"))

        then:
        1 * tradingService.process(_, tradingRequest, tradingResult) >> { TradingQuote tradingQuote, request, result ->
            assert tradingQuote.currentPrice == 2.54
            return tradingResult
        }
        0 * webSocket.disconnect()

        when:
        productUpdateListener.onTextMessage(webSocket, Messages.getTradingQuoteMessage(PRODUCT_ID,"2.75"))

        then:
        1 * tradingService.process(_, tradingRequest, tradingResult) >> { TradingQuote tradingQuote, request, result ->
            assert tradingQuote.currentPrice == 2.75
            def newResult = new TradingResult(result)
            newResult.isSold = true
            return newResult
        }
        1 * webSocket.disconnect()

    }

}