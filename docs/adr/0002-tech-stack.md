# ADR 0002: Core Technology Stack

- **Status:** Accepted
- **Date:** 2026-11-25

## Context

Perfume Advisor is a multi-user web application with relational data (users, shared catalog, collections, wear logs), rule-based recommendation logic, and external data ingestion. As a final-year CS learning project, the stack should use modern, industry-relevant, long-term-supported tools without unnecessary operational complexity.

## Decision

Adopt the following core stack (as specified in `docs/SRS.md`):

- **Backend:** Java 25 (LTS) with Spring Boot 4.x and Maven (with Maven Wrapper).
- **Database & Migrations:** PostgreSQL (with `pg_trgm` for fuzzy name search) and Flyway for versioned SQL migrations (`ddl-auto: validate` in Hibernate).
- **Testing:** JUnit 5, Mockito, AssertJ, Spring MockMvc, and Testcontainers against real PostgreSQL.
- **Frontend (M1+):** React with TypeScript (Vite) and Tailwind CSS.
- **Local Dev & CI:** Docker Compose for local PostgreSQL and GitHub Actions for automated builds and verification.

## Consequences

- **Positive:**
  - Java 25 LTS and Spring Boot 4 provide modern language features, strong typing, and an established ecosystem for REST APIs, security, and data access.
  - Flyway ensures schema changes are explicit, repeatable, and identical between local development, CI, and production.
  - Testcontainers catches PostgreSQL-specific SQL issues early instead of relying on an in-memory H2 database.
- **Negative:**
  - Spring Boot 4 introduced breaking package and starter changes (e.g., `@MockitoBean`, modularized test starters), so older tutorials cannot be copied blindly and must be verified against current Spring Boot 4 documentation.
  - Running Testcontainers in CI and locally requires Docker.
