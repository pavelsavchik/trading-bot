package com.getbux.common

class Messages {

    public static String getConnectMessage() {
        return """
            {
                "t": "connect.connected",
                "body": {}
            }
        """
    }

    public static String getConnectFailedMessage() {
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

    public static String getSubscriptionMessage(String productId) {
        return """
            {
               "subscribeTo": [
                  "trading.product.$productId"
               ],
               "unsubscribeFrom": null
            }
        """
    }

    public static String getTradingQuoteMessage(String currentPrice, String productId) {
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

    static getBuyResponse(String positionId = "positionId") {
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
