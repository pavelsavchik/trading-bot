package com.getbux.utils;

import static com.getbux.configuration.AppConfiguration.PRICE_PRECISION;

public class PriceUtils {

    public static boolean equals(Double first, Double second) {
        Double diff = Math.abs(first - second);
        return diff < PRICE_PRECISION;
    }

    public static boolean inRange(Double value, Double lowerBound, Double upperBound) {
        return (upperBound == null || value < upperBound) &&
                (lowerBound == null ||  value > lowerBound);
    }

}