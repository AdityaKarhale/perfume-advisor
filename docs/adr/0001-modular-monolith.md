# ADR 0001: Modular Monolith Architecture

- **Status:** Accepted
- **Date:** 2026-11-25

## Context

Perfume Advisor has several distinct domain areas defined in `docs/SRS.md`: authentication (`auth`), shared perfume catalog (`catalog`), user collections (`collection`), external data ingestion (`ingestion`), rule-based scoring (`recommendation`), and wear history (`wearlog`).

We need an architecture that is easy for a solo developer to build, test, and deploy on free/low-cost hosting tiers while keeping domain boundaries clean so features do not turn into tangled spaghetti code.

## Decision

Build the backend as a **modular monolith** inside a single Spring Boot application, paired with a separate React frontend in the same monorepo.

- Each domain lives in its own top-level feature package under `com.perfumeadvisor.<module>` (for example `ping`, `auth`, `catalog`, `collection`, `ingestion`, `recommendation`, `wearlog`).
- Inside each module, code is organized into `controller`, `service`, and `repository` layers.
- Modules share a single PostgreSQL database and communicate through in-process service calls rather than network requests.

## Consequences

- **Positive:**
  - Single backend artifact to build, test, and deploy (fits free-tier hosting like Render + Neon).
  - No distributed-systems overhead (no service discovery, message brokers, or cross-service network failures).
  - Refactoring across modules is fast and type-safe within one codebase.
- **Negative:**
  - Discipline is required to respect module boundaries and avoid reaching directly into another module's repositories.
  - All modules scale together as one deployable unit (acceptable for this project's expected traffic).
