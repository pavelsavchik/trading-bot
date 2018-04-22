package com.getbux.api;

public class BuyRequest {

    private String productId;

    private InvestingAmount investingAmount;

    private Integer leverage;

    private String direction;

    public BuyRequest(String productId, InvestingAmount investingAmount, Integer leverage, String direction) {
        this.productId = productId;
        this.investingAmount = investingAmount;
        this.leverage = leverage;
        this.direction = direction;
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

}
