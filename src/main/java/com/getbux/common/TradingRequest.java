package com.getbux.common;

public class TradingRequest {

    private String productId;
    private Double buyPrice;
    private Double upperLimitSellPrice;
    private Double lowerLimitSellPrice;

    public TradingRequest() { }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getUpperLimitSellPrice() {
        return upperLimitSellPrice;
    }

    public void setUpperLimitSellPrice(Double upperLimitSellPrice) {
        this.upperLimitSellPrice = upperLimitSellPrice;
    }

    public Double getLowerLimitSellPrice() {
        return lowerLimitSellPrice;
    }

    public void setLowerLimitSellPrice(Double lowerLimitSellPrice) {
        this.lowerLimitSellPrice = lowerLimitSellPrice;
    }
}