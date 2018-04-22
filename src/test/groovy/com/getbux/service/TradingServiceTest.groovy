package com.getbux.service

import com.getbux.api.TradingAPIClient
import com.getbux.common.TradingRequest
import com.getbux.socket.TradingQuote
import spock.lang.Specification

class TradingServiceTest extends Specification {

    def apiClient = Mock(TradingAPIClient)

    def tradingService = new TradingService(apiClient)

    def "test buy and sell with high price"() {
        given:
        final tradingRequest = new TradingRequest(
                productId:"sb12345",
                buyPrice: 10.4,
                upperLimitSellPrice: 11.2,
                lowerLimitSellPrice: 9.6
        )

        when:
        def result = tradingService.process(new TradingQuote(currentPrice: 10.5), tradingRequest, null)

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        !result.positionId
        !result.sold

        when:
        result = tradingService.process(new TradingQuote(currentPrice: 10.4), tradingRequest, result)

        then:
        1 * apiClient.buy(tradingRequest) >> "positionId"
        0 * apiClient.sell(_)
        result.positionId == "positionId"
        !result.sold

        when:
        result = tradingService.process(new TradingQuote(currentPrice: 10.4), tradingRequest, result)

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        result.positionId == "positionId"
        !result.sold

        when:
        result = tradingService.process(new TradingQuote(currentPrice: 12), tradingRequest, result)

        then:
        0 * apiClient.buy(_)
        1 * apiClient.sell(tradingRequest)
        result.positionId == "positionId"
        result.sold
    }

    def "test buy and sell with low price"() {
        given:
        final tradingRequest = new TradingRequest(
                productId:"sb12345",
                buyPrice: 10.4,
                upperLimitSellPrice: 11.2,
                lowerLimitSellPrice: 9.6
        )

        when:
        def result = tradingService.process(new TradingQuote(currentPrice: 10.5), tradingRequest, null)

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        !result.positionId
        !result.sold

        when:
        result = tradingService.process(new TradingQuote(currentPrice: 10.4), tradingRequest, result)

        then:
        1 * apiClient.buy(tradingRequest) >> "positionId"
        0 * apiClient.sell(_)
        result.positionId == "positionId"
        !result.sold

        when:
        result = tradingService.process(new TradingQuote(currentPrice: 10.4), tradingRequest, result)

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        result.positionId == "positionId"
        !result.sold

        when:
        result = tradingService.process(new TradingQuote(currentPrice: 9.2), tradingRequest, result)

        then:
        0 * apiClient.buy(_)
        1 * apiClient.sell(tradingRequest)
        result.positionId == "positionId"
        result.sold
    }

}
