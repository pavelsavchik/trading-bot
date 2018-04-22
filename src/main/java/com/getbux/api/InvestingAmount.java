package com.getbux.api;

public class InvestingAmount {

    private String currency;
    private Integer decimals;
    private String amount;

    public InvestingAmount(String currency, Integer decimals, String amount) {
        this.currency = currency;
        this.decimals = decimals;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
