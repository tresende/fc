package com.tresende.catalog.admin.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleStorageProperties implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(GoogleStorageProperties.class);

    private String bucket;
    private int connectTimeout;
    private int readTimeout;
    private int retryDelay;
    private int retryMaxDelay;
    private int retryMaxAttempts;
    private double retryMultiplier;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(final int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public int getRetryMaxDelay() {
        return retryMaxDelay;
    }

    public void setRetryMaxDelay(final int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public void setRetryMaxAttempts(final int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
    }

    public void setRetryMultiplier(final double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }

    @Override
    public void afterPropertiesSet() {
        logger.debug(toString());
    }

    @Override
    public String toString() {
        return "GoogleStorageProperties{" +
                "bucket='" + bucket + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", retryDelay=" + retryDelay +
                ", retryMaxDelay=" + retryMaxDelay +
                ", retryMaxAttempts=" + retryMaxAttempts +
                ", retryMultiplier=" + retryMultiplier +
                '}';
    }
}
