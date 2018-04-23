package com.getbux.integration

import com.getbux.api.TradingAPIClient
import com.getbux.service.TradingService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.lang.Specification

@Configuration
class MockBeansConfiguration extends Specification {

    @Bean
    TradingAPIClient apiClient() {
        return Mock(TradingAPIClient)
    }

    @Bean
    TradingService tradingService(TradingAPIClient apiClient) {
        return new TradingService(apiClient)
    }

}