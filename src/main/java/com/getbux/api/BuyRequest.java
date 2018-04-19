package com.getbux.api;

class BuyRequest {

    private String productId;

    private InvestingAmount investingAmount = new InvestingAmount();

    private Integer leverage = 2;

    private String direction = "BUY";

    private class InvestingAmount {
        private String currency = "BUX";
        private Integer decimals = 2;
        private String amount = "200.00";
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public InvestingAmount getInvestingAmount() {
        return investingAmount;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public String getDirection() {
        return direction;
    }

    BuyRequest(String productId) {
        this.productId = productId;
    }

}
