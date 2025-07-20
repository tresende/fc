package com.tresende.catalog.infrastructure.configuration.properties;

public class RestClientProperties {

    private String baseUrl;
    private int readTimeout;

    public String baseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int readTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
