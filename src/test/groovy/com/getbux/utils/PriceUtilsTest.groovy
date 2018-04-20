package com.getbux.utils

import spock.lang.Specification
import spock.lang.Unroll

class PriceUtilsTest extends Specification {

    @Unroll
    def "test inRange"() {

        def result

        when:
        result = PriceUtils.inRange(value, min, max)

        then:
        result == expectedResult

        where:
        value | min | max | expectedResult
        5.0   | 0.0 | 6.0 | true
        5.0   | 5.0 | 6.0 | false
        5.0   | 4.9 | 6.0 | true
        5.0   | 4.9 | 5.0 | false
        5.0   | 4.9 | 5.1 | true
    }

}
