package com.getbux.integration

import com.getbux.api.TradingAPIClient
import com.getbux.common.Messages
import com.getbux.common.TradingRequest
import com.getbux.service.TradingService
import com.getbux.socket.TradingQuoteListener
import com.neovisionaries.ws.client.WebSocket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification

@ContextConfiguration(classes = [MockBeansConfiguration.class], loader = AnnotationConfigContextLoader.class)
class TradingTest extends Specification {

    @Autowired
    TradingAPIClient tradingAPIClient

    @Autowired
    TradingService tradingService

    def "test normal trading flow"() {
        given:
        def request = new TradingRequest(
                productId: "sb12345",
                buyPrice: 12.2,
                upperLimitSellPrice: 12.5,
                lowerLimitSellPrice: 11
        )
        def listener = new TradingQuoteListener(request, tradingService)
        def socket = Mock(WebSocket)

        when:
        listener.onTextMessage(socket, Messages.getConnectMessage())

        then:
        0 * tradingAPIClient._

        when:
        listener.onTextMessage(socket, Messages.getTradingQuoteMessage("11.5", "sb12345"))

        then:
        0 * tradingAPIClient._

        when:
        listener.onTextMessage(socket, Messages.getTradingQuoteMessage("12.2", "sb12345"))

        then:
        1 * tradingAPIClient.buy("sb12345") >> "positionId"

        when:
        listener.onTextMessage(socket, Messages.getTradingQuoteMessage("11.5", "sb12345"))

        then:
        0 * tradingAPIClient._

        when:
        listener.onTextMessage(socket, Messages.getTradingQuoteMessage("11", "sb12345"))

        then:
        1 * tradingAPIClient.sell("positionId")
        1 * socket.disconnect()
    }

}
