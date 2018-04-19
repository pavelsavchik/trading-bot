package com.getbux.socket;

import java.util.Collections;
import java.util.List;

public class SubscriptionRequest {

    private List<String> subscribeTo;

    private List<String> unsubscribeFrom;

    public SubscriptionRequest(String productId) {
        String subscribeRequest = "trading.product." + productId;
        subscribeTo = Collections.singletonList(subscribeRequest);
    }

    public List<String> getSubscribeTo() {
        return subscribeTo;
    }

    public void setSubscribeTo(List<String> subscribeTo) {
        this.subscribeTo = subscribeTo;
    }

    public List<String> getUnsubscribeFrom() {
        return unsubscribeFrom;
    }

    public void setUnsubscribeFrom(List<String> unsubscribeFrom) {
        this.unsubscribeFrom = unsubscribeFrom;
    }
}
