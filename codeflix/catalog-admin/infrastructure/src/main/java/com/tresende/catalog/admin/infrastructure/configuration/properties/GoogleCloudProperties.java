package com.tresende.catalog.admin.infrastructure.configuration.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleCloudProperties implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudProperties.class);

    private String credentials;
    private String projectId;

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(final String credentials) {
        this.credentials = credentials;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "GoogleCloudProperties{" +
                ", projectId='" + projectId + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() {
        logger.debug(toString());
    }
}
