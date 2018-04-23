package com.getbux.api

import org.apache.commons.io.IOUtils
import org.apache.http.Header
import org.apache.http.ProtocolVersion
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicStatusLine
import org.skyscreamer.jsonassert.JSONAssert
import spock.lang.Specification

class TradingAPIClientTest extends Specification {

    def httpClient = Mock(CloseableHttpClient)

    def apiClient = new TradingAPIClient(httpClient)

    def "test buy"() {
        given:
        def response = Mock(CloseableHttpResponse)
        response.getStatusLine() >> new BasicStatusLine(Mock(ProtocolVersion), 200, "OK")
        response.getEntity() >> new StringEntity(getBuyResponse("positionId"))

        when:
        String positionId = apiClient.buy("productId")

        then:
        1 * httpClient.execute(_) >> { HttpUriRequest request ->
            assert request.getMethod() == "POST"
            assert request.URI.toString() == "https://api.beta.getbux.com/core/16/users/me/trades"
            JSONAssert.assertEquals(getBody(request), getBuyRequest("productId"), true)
            assertHeaders(request.getAllHeaders())

            return response
        }
        positionId == "positionId"
    }

    def "test buy with error response"() {
        given:
        def response = Mock(CloseableHttpResponse)
        response.getStatusLine() >> new BasicStatusLine(Mock(ProtocolVersion), 400, "Fail")
        response.getEntity() >> new StringEntity(getBuyResponse("positionId"))

        when:
        String positionId = apiClient.buy("productId")

        then:
        1 * httpClient.execute(_) >> { HttpUriRequest request ->
            assert request.getMethod() == "POST"
            assert request.URI.toString() == "https://api.beta.getbux.com/core/16/users/me/trades"
            JSONAssert.assertEquals(getBody(request), getBuyRequest("productId"), true)
            assertHeaders(request.getAllHeaders())

            return response
        }
        !positionId
    }

    def "test buy with null productId"() {
        when:
        String positionId = apiClient.buy(null)

        then:
        0 * httpClient.execute(_)
        !positionId
    }

    def "test sell"() {
        given:
        def response = Mock(CloseableHttpResponse)
        response.getStatusLine() >> new BasicStatusLine(Mock(ProtocolVersion), 200, "OK")
        response.getEntity() >> new StringEntity("{}")

        when:
        def result = apiClient.sell("somepositionid")

        then:
        1 * httpClient.execute(_) >> { HttpUriRequest request ->
            assert request.getMethod() == "DELETE"
            assertHeaders(request.getAllHeaders())
            assert request.URI.toString() == "https://api.beta.getbux.com/core/16/users/me/portfolio/positions/somepositionid"
            return response
        }
        result
    }

    def "test sell with null positionId"() {
        when:
        apiClient.sell(null)

        then:
        0 * httpClient.execute(_)
    }

    def "test sell with error response"() {
        given:
        def response = Mock(CloseableHttpResponse)
        response.getStatusLine() >> new BasicStatusLine(Mock(ProtocolVersion), 400, "OK")
        response.getEntity() >> new StringEntity("{}")

        when:
        def result = apiClient.sell("somepositionid")

        then:
        1 * httpClient.execute(_) >> { HttpUriRequest request ->
            assert request.getMethod() == "DELETE"
            assertHeaders(request.getAllHeaders())
            assert request.URI.toString() == "https://api.beta.getbux.com/core/16/users/me/portfolio/positions/somepositionid"
            return response
        }
        !result
    }

    private void assertHeaders(Header[] headers) {
        assert headers.size() == 4
        assert headers.any {
            it.name == "Authorization" && it.value == "Bearer " +
                    "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWE" +
                    "xMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInN" +
                    "jcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE" +
                    "1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiI" +
                    "sImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg"
        }
        assert headers.any { it.name == "Accept-Language" && it.value == "nl-NL,en;q=0.8" }
        assert headers.any { it.name == "Content-Type" && it.value == "application/json" }
        assert headers.any { it.name == "Accept" && it.value == "application/json" }
    }

    private String getBody(HttpUriRequest request) {
        return IOUtils.toString((request as HttpPost).entity.content, "UTF-8")
    }

    private getBuyRequest(String productId = "productId") {
        """
            {
                "productId" : "$productId",
                "investingAmount" : {
                    "currency": "BUX",
                    "decimals": 2,
                    "amount": "200.00"
                },
                "leverage" : 2,
                "direction" : "BUY"
            }
        """
    }

    private getBuyResponse(String positionId = "positionId") {
        """
        {
            "id": "98922f1a-4c10-4635-a9e6-ae19ddcd12b4",
            "positionId": "$positionId",
            "product": {
                "securityId": "{productId}",
                "symbol": "{productSymbol)",
                "displayName": "{productName}"
            },
            "investingAmount": {
                "currency": "BUX",
                "decimals": 2,
                "amount": "200.00"
            }, 
            "price": {
                "currency": "EUR",
                "decimals": 3,
                "amount": "0.567"
            },
            "leverage": 1,
            "direction": "BUY",
            "type": "OPEN",
            "dateCreated": 1405515165705
        }
        """
    }
}