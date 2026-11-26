package com.perfumeadvisor.ping.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides application identity information (name and version).
 *
 * This is a @Service — a Spring-managed bean for business logic.
 * The @Value annotations inject values from application.properties at startup.
 * Constructor injection is used (the constructor parameters are automatically resolved
 * by Spring) which makes the class easy to unit-test: you just pass values directly.
 */
@Service
public class PingService {

    private final String appName;
    private final String appVersion;

    public PingService(
            @Value("${spring.application.name}") String appName,
            @Value("${app.version}") String appVersion) {
        this.appName = appName;
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }
}
