package com.getbux.socket;

public class TradingResult implements Cloneable {

    private String positionId;

    private boolean isSold;

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
            this.isSold = original.isSold;
            this.positionId = original.positionId;
        }
    }
}
