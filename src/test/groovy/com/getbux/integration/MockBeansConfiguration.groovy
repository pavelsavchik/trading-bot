package com.getbux.integration

import com.getbux.api.TradingClient
import com.getbux.service.TradingService
import com.getbux.service.PriceBasedTradingService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.lang.Specification

@Configuration
class MockBeansConfiguration extends Specification {

    @Bean
    TradingClient client() {
        return Mock(TradingClient)
    }

    @Bean
    TradingService tradingService(TradingClient tradingClient) {
        return new PriceBasedTradingService(tradingClient)
    }

}