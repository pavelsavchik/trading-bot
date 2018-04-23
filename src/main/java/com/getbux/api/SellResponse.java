package com.getbux.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellResponse {

    private String profitAndLossAmount;

    @SuppressWarnings("unchecked")
    @JsonProperty("profitAndLoss")
    private void unpackNested(Map<String,Object> profitAndLoss) {
        this.profitAndLossAmount = (String)profitAndLoss.get("amount");
    }

    public String getProfitAndLossAmount() {
        return profitAndLossAmount;
    }

    public void setProfitAndLossAmount(String profitAndLossAmount) {
        this.profitAndLossAmount = profitAndLossAmount;
    }
}
