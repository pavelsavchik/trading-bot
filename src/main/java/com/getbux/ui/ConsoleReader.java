package com.getbux.ui;

import com.getbux.utils.PriceUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Scanner;

class ConsoleReader {

    private Scanner scanner;

    ConsoleReader() {
        scanner = new Scanner(System.in);
    }

    String readString() {
        return scanner.nextLine();
    }

    Double readDouble(Double min, Double max) {
        String error = null;
        Double value = null;
        do {
            try {
                value = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException exception) {
                error = "Invalid value";
                logError(error);
                continue;
            }
            error = PriceUtils.inRange(value, min, max) ? null : buildDoubleError(min, max);
            logError(error);
        } while (StringUtils.isNotEmpty(error));

        return value;
    }

    Double readDouble(Double min) {
        return readDouble(min, null);
    }

    private String buildDoubleError(Double min, Double max) {
        String message = "Value should be greater than " + min;
        if (max != null) {
            message += " and less than " + max;
        }
        return message;
    }

    private void logError(String error) {
        if (StringUtils.isNotEmpty(error)) {
            System.out.println(error + ", please try again");
        }
    }

}
