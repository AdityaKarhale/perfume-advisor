package com.perfumeadvisor;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test verifying that the application context starts with a real
 * PostgreSQL database and that Flyway automatically applies all schema migrations.
 *
 * Uses Testcontainers to spin up an isolated, disposable PostgreSQL 17 container.
 * Spring Boot's @ServiceConnection automatically wires the container's dynamic JDBC
 * connection details into the DataSource and Flyway without manual configuration.
 */
@SpringBootTest
@Testcontainers
class DatabaseMigrationIntegrationTest {

    static {
        System.setProperty("user.timezone", "UTC");
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("UTC"));
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Application context loads and PostgreSQL container is running")
    void contextLoads() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Flyway applies V1 migration and enables pg_trgm extension")
    void flywayAppliedV1Migration() {
        // 1. Verify via Flyway metadata API that V1 migration was applied successfully
        MigrationInfo[] applied = flyway.info().applied();
        assertThat(applied)
                .as("Flyway applied migrations list")
                .isNotEmpty();

        MigrationInfo v1 = applied[0];
        assertThat(v1.getVersion().getVersion()).isEqualTo("1");
        assertThat(v1.getDescription()).isEqualTo("enable pg trgm");
        assertThat(v1.getState().isApplied()).isTrue();

        // 2. Verify directly in PostgreSQL that the pg_trgm extension is actually active
        Integer extensionCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM pg_extension WHERE extname = 'pg_trgm'",
                Integer.class
        );
        assertThat(extensionCount)
                .as("pg_trgm extension count in pg_extension")
                .isEqualTo(1);
    }
}
