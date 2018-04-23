package com.getbux.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.getbux.common.Messages
import spock.lang.Specification

class SellResponseMapping extends Specification {

    def objectMapper = new ObjectMapper()

    def "test profitAndLossAmount mapping"() {
        when:
        def sellResponse = objectMapper.readValue(Messages.getSellResponse("123.45"), SellResponse.class)

        then:
        sellResponse.profitAndLossAmount == "123.45"
    }

}
