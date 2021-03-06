package com.getbux.configuration;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.entity.StringEntity;

import java.io.File;

public class AppConfiguration {

    public static final String API_URL;

    public static final String BUY_PATH;

    public static final String BUY_URL;

    public static final String SELL_PATH;

    public static final String SELL_URL;

    public static final String SUBSCRIPTION_URL;

    public static final Integer SOCKET_CONNECTION_TIMEOUT;

    public static final Integer SOCKET_CONNECTION_ATTEMPTS;

    public static final Integer BUY_REQUEST_LEVERAGE;

    public static final String BUY_REQUEST_DIRECTION;

    public static final String BUY_REQUEST_CURRENCY;

    public static final Integer BUY_REQUEST_DECIMALS;

    public static final String BUY_REQUEST_AMOUNT;

    public static final Double PRICE_PRECISION;

    public static final String AUTH_HEADER_NAME;

    public static final String AUTH_HEADER_VALUE;

    public static final String LANGUAGE_HEADER_NAME;

    public static final String LANGUAGE_HEADER_VALUE;

    public static final String CONTENT_TYPE_HEADER_NAME;

    public static final String CONTENT_TYPE_HEADER_VALUE;

    public static final String ACCEPT_HEADER_NAME;

    public static final String ACCEPT_HEADER_VALUE;

    static {
        Configurations configs = new Configurations();
        Configuration config = null;

        try {
            config = configs.properties(new File("application.properties"));
        } catch (ConfigurationException e) {
            terminate();
        }

        if(config == null) {
            terminate();
        }

        API_URL = config.getString("api.url");
        BUY_PATH = config.getString("api.buy.path");
        SELL_PATH = config.getString("api.sell.path");
        BUY_URL = API_URL + BUY_PATH;
        SELL_URL = API_URL + SELL_PATH;

        SUBSCRIPTION_URL = config.getString("socket.subscription.url");
        SOCKET_CONNECTION_TIMEOUT = config.getInt("socket.connection.timeout");
        SOCKET_CONNECTION_ATTEMPTS = config.getInt("socket.connection.attempts");

        BUY_REQUEST_LEVERAGE = config.getInt("buy.request.leverage");
        BUY_REQUEST_DIRECTION = config.getString("buy.request.direction");
        BUY_REQUEST_CURRENCY = config.getString("buy.request.currency");
        BUY_REQUEST_DECIMALS = config.getInt("buy.request.decimals");
        BUY_REQUEST_AMOUNT = config.getString("buy.request.amount");

        PRICE_PRECISION = config.getDouble("price.precision");

        AUTH_HEADER_NAME = config.getString("headers.auth.name");
        AUTH_HEADER_VALUE = config.getString("headers.auth.value");
        LANGUAGE_HEADER_NAME = config.getString("headers.language.name");
        LANGUAGE_HEADER_VALUE = config.getString("headers.language.value");
        CONTENT_TYPE_HEADER_NAME = config.getString("headers.contentType.name");
        CONTENT_TYPE_HEADER_VALUE = config.getString("headers.contentType.value");
        ACCEPT_HEADER_NAME = config.getString("headers.accept.name");
        ACCEPT_HEADER_VALUE = config.getString("headers.accept.value");

    }

    private static void terminate() {
        System.out.println("Failed configuration loading");
        System.exit(0);
    }

}