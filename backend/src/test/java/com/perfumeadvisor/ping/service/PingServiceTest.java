package com.perfumeadvisor.ping.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Plain unit test for PingService — no Spring context needed.
 *
 * Because PingService uses constructor injection, we can create it directly
 * with known values and verify the getters. This is fast (milliseconds)
 * and proves the logic works in isolation.
 */
class PingServiceTest {

    @Test
    void shouldReturnConfiguredAppName() {
        PingService service = new PingService("Test App", "1.2.3");

        assertThat(service.getAppName()).isEqualTo("Test App");
    }

    @Test
    void shouldReturnConfiguredAppVersion() {
        PingService service = new PingService("Test App", "1.2.3");

        assertThat(service.getAppVersion()).isEqualTo("1.2.3");
    }
}
