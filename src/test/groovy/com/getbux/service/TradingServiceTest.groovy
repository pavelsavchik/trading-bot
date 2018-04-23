package com.getbux.service

import com.getbux.api.TradingAPIClient
import com.getbux.common.TradingRequest
import com.getbux.socket.TradingQuote
import com.getbux.socket.TradingResult
import spock.lang.Specification

class TradingServiceTest extends Specification {

    final apiClient = Mock(TradingAPIClient)

    final tradingService = new TradingService(apiClient)

    final tradingRequest = new TradingRequest(
            productId:"sb12345",
            buyPrice: 10.4,
            upperLimitSellPrice: 11.2,
            lowerLimitSellPrice: 9.6
    )

    def "test buy when price does not match"() {
        when:
        def result = tradingService.process(new TradingQuote(currentPrice: 10.5), tradingRequest, null)

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        !result.positionId
        !result.sold
    }

    def "test buy when price matches"() {
        when:
        def result = tradingService.process(new TradingQuote(currentPrice: 10.4), tradingRequest, null)

        then:
        1 * apiClient.buy(tradingRequest.getProductId()) >> "positionId"
        0 * apiClient.sell(_)
        result.positionId == "positionId"
        !result.sold
    }

    def "test buy when price matches buy buying failed"() {
        when:
        def result = tradingService.process(new TradingQuote(currentPrice: 10.4), tradingRequest, null)

        then:
        1 * apiClient.buy(tradingRequest.getProductId()) >> null
        0 * apiClient.sell(_)
        !result.positionId
        !result.sold
    }


    def "test sell when price in range"() {
        when:
        def result = tradingService.process(
                new TradingQuote(currentPrice: 10.4),
                tradingRequest,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        result.positionId == "positionId"
        !result.sold
    }

    def "test null quote"() {
        when:
        def result = tradingService.process(
                null,
                tradingRequest,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        !result
    }

    def "test null request"() {
        when:
        def result = tradingService.process(
                new TradingQuote(currentPrice: 10.4),
                null,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        0 * apiClient.sell(_)
        !result
    }

    def "test sell when price is higher"() {
        when:
        def result = tradingService.process(
                new TradingQuote(currentPrice: 14),
                tradingRequest,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        1 * apiClient.sell("positionId") >> true
        result.positionId == "positionId"
        result.sold
    }

    def "test sell when price is higher but selling failed"() {
        when:
        def result = tradingService.process(
                new TradingQuote(currentPrice: 14),
                tradingRequest,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        1 * apiClient.sell("positionId") >> false
        result.positionId == "positionId"
        !result.sold
    }

    def "test sell when price is lower"() {
        when:
        def result = tradingService.process(
                new TradingQuote(currentPrice: 8.66),
                tradingRequest,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        1 * apiClient.sell("positionId") >> true
        result.positionId == "positionId"
        result.sold
    }

    def "test sell when price is lower but selling failed"() {
        when:
        def result = tradingService.process(
                new TradingQuote(currentPrice: 8.66),
                tradingRequest,
                new TradingResult(positionId: "positionId", isSold: false)
        )

        then:
        0 * apiClient.buy(_)
        1 * apiClient.sell("positionId") >> false
        result.positionId == "positionId"
        !result.sold
    }

    def "test full flow"() {
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
        1 * apiClient.buy(tradingRequest.getProductId()) >> "positionId"
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
        1 * apiClient.sell("positionId") >> true
        result.positionId == "positionId"
        result.sold
    }

}
