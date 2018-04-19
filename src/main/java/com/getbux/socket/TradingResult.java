package com.getbux.socket;

public class TradingResult implements Cloneable {

    private boolean isBought;

    private String positionId;

    private boolean isSold;

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public TradingResult() {
    }

    public TradingResult(TradingResult original) {
        if(original != null) {
            this.isBought = original.isBought;
            this.isSold = original.isSold;
            this.positionId = original.positionId;
        }
    }
}
