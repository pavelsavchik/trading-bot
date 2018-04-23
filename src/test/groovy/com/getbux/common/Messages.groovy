package com.getbux.common

class Messages {

    static String getConnectMessage() {
        return """
            {
                "t": "connect.connected",
                "body": {}
            }
        """
    }

    static String getConnectFailedMessage() {
        return """
            {
                "t": "connect.failed",
                "body": {
                    "developerMessage": "Missing JWT Access Token in request",
                    "errorCode": "RTF_002"
                }
            }
        """
    }

    static String getSubscriptionMessage(String productId) {
        return """
            {
               "subscribeTo": [
                  "trading.product.$productId"
               ],
               "unsubscribeFrom": null
            }
        """
    }

    static String getTradingQuoteMessage(String productId, String currentPrice = "2.52") {
        return """
            {
               "t": "trading.quote",
               "body": {
                  "securityId": "$productId",
                  "currentPrice": "$currentPrice"
               }
            }        
        """
    }

    static String getBuyRequest(String productId = "productId") {
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

    static String getBuyResponse(String positionId = "positionId") {
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

    static String getSellResponse(String amount = "225.01") {
        return """
            {
                "id": "ebd37d2b-8489-4419-bd78-8df7dc6a4823",
                "positionId": "423ac625-bd9a-41eb-8531-d5ea25352020",
                "profitAndLoss": {
                    "currency": "BUX",
                    "decimals": 2,
                    "amount": "$amount"
                },
                "product": {
                    "securityId": "sb26493",
                    "symbol": "GERMANY30",
                    "displayName": "Germany 30"
                },
                "investingAmount": {
                    "currency": "BUX",
                    "decimals": 2,
                    "amount": "200.00"
                }, "price": {
                    "currency": "EUR",
                    "decimals": 1,
                    "amount": "10342.5"
                },
                "leverage": 2,
                "direction": "SELL",
                "type": "CLOSE",
                "dateCreated": 1473946766125
            }
        """
    }
}
