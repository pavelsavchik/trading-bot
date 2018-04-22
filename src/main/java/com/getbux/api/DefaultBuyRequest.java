package com.getbux.api;

import static com.getbux.configuration.AppConfiguration.*;

public class DefaultBuyRequest extends BuyRequest {

    DefaultBuyRequest(String productId) {
        super(productId, new InvestingAmount(
                        BUY_REQUEST_CURRENCY,
                        BUY_REQUEST_DECIMALS,
                        BUY_REQUEST_AMOUNT),
                BUY_REQUEST_LEVERAGE,
                BUY_REQUEST_DIRECTION);
    }

}
