package com.getbux.utils;

public class PriceUtils {

    private static final Double precision = 0.001;

    public static boolean equals(Double first, Double second) {
        Double diff = Math.abs(first - second);
        return diff < precision;
    }

    public static boolean inRange(Double value, Double lowerBound, Double upperBound) {
        return (upperBound == null || value < upperBound) &&
                (lowerBound == null ||  value > lowerBound);
    }

}