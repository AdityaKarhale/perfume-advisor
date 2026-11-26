# Perfume Advisor – Software Requirements Specification (v0.1)

Prepared by: Aditya Karhale · Client: Aditya  · Status: Draft for client review

## 1. Introduction

### 1.1 Purpose

This document defines what the Perfume Advisor web application must do. It is the single source of truth for design, development and testing.

### 1.2 Scope

Perfume Advisor is a public, multi-user web application. Each user builds a personal perfume collection. The system gathers each perfume's characteristics (notes, scent family, projection, strength, longevity, suitable seasons and occasions) and recommends the **top 3 perfumes** from the user's own collection for a chosen occasion, using live weather, season and time of day. Users can log what they wore, rate it and add notes, and this history improves later recommendations.

### 1.3 Definitions

- **Catalog**: the shared database of perfumes and their characteristics, common to all users.
- **Collection**: the set of catalog perfumes a particular user owns.
- **Wear log**: a user's record of wearing a perfume, with rating and notes.
- **Accord**: a group of notes forming a scent theme (e.g. woody, citrus, sweet).
- **Projection**: how far the scent travels from the wearer. **Longevity**: how long it lasts.

## 2. Overall Description

### 2.1 Users

A single user role: a registered perfume owner. (An admin role may be added later for catalog moderation.)

### 2.2 Operating environment

Responsive web app for modern browsers on mobile and desktop. Internet connection required.

### 2.3 Constraints and assumptions

- Backend is written in Java.
- No official free perfume-data API exists, so data is gathered through a pluggable ingestion pipeline (scraping among other sources). Scraping must respect robots.txt, rate limits and site terms, and must store only factual attributes, never copied descriptions or reviews.
- Collections are small (about 10 to 20 perfumes per user), so recommendation logic runs in real time.
- Hosting uses free or low-cost tiers.

## 3. Functional Requirements

### 3.1 Accounts and authentication

- **FR-1**: A visitor can register with email and password.
- **FR-2**: A registered user can log in and log out securely.
- **FR-3**: A user can reset a forgotten password.
- **FR-4**: A user can only see and modify their own collection, logs and settings.
- **FR-5**: A user can delete their account and all associated data.

### 3.2 Collection management

- **FR-6**: A user can add a perfume by searching name and brand.
- **FR-7**: If the perfume is already in the catalog, it is linked to the user's collection. Otherwise the system triggers data ingestion.
- **FR-8**: A user can view their collection as a list with key attributes.
- **FR-9**: A user can open a perfume's detail page showing all characteristics.
- **FR-10**: A user can edit or override any characteristic for their own copy (personal override).
- **FR-11**: A user can remove a perfume from their collection.
- **FR-12**: A user can add a perfume manually if automatic ingestion fails.

### 3.3 Perfume data ingestion

- **FR-13**: The system gathers notes (top, middle, base), accords, projection, strength (concentration), longevity, and seasonal and occasion suitability.
- **FR-14**: Ingestion supports multiple data sources behind one interface, with fallback when one fails.
- **FR-15**: Ingestion runs asynchronously. The user sees a status (pending, ready or failed) and can retry.
- **FR-16**: Ingested data is cached in the catalog so each perfume is fetched only once.
- **FR-17**: Ingestion obeys rate limits and logs every failure with its reason.

### 3.4 Recommendation

- **FR-18**: A user selects an occasion from a fixed list (for example office, formal meeting, evening date, house party, wedding, casual outing, gym, travel) and can search or filter that list.
- **FR-19**: The system obtains current weather for the user's location, taken from browser geolocation or a manually entered city.
- **FR-20**: The system uses season and time of day (auto-detected, user can override).
- **FR-21**: The system returns the **top 3** perfumes from the user's collection, ranked, each with a short human-readable reason.
- **FR-22**: Scoring is rule-based and explainable, combining occasion fit, weather and season fit, time-of-day fit, and an adjustment from the user's past ratings.
- **FR-23**: A user setting **"rotate my perfumes"** (on or off) lowers the score of recently worn perfumes when enabled.
- **FR-24**: If the collection has fewer than 3 suitable perfumes, the system returns what it has and says so.

### 3.5 Wear log and feedback

- **FR-25**: A user can log wearing a perfume with date, occasion, and optionally weather.
- **FR-26**: A user can rate a log entry (1 to 5) and add a free-text note, for example "got a compliment".
- **FR-27**: A user can log directly from a recommendation with one tap.
- **FR-28**: A user can view, edit and delete their log history.
- **FR-29**: A user can see simple stats, such as most worn and highest rated perfumes.

## 4. Non-Functional Requirements

- **NFR-1 Security**: Passwords are hashed with a strong algorithm. All traffic uses HTTPS. Inputs are validated. The system follows OWASP Top 10 guidance and protects against injection, XSS and CSRF.
- **NFR-2 Privacy**: Store only the minimum personal data (email, collection, logs). Location is used for weather only and is not stored.
- **NFR-3 Performance**: Recommendation responds within 2 seconds. Ordinary pages load within 3 seconds on a mobile connection.
- **NFR-4 Reliability**: A failing external source (scraper or weather API) never breaks the app. The system degrades gracefully with a clear message.
- **NFR-5 Maintainability**: Layered architecture, automated tests, consistent code style, and documented decisions.
- **NFR-6 Testability**: Core logic (scoring, ingestion parsing) is covered by automated unit tests. Key flows by integration tests.
- **NFR-7 Usability**: Mobile-first responsive layout, usable one-handed, with readable contrast.
- **NFR-8 Observability**: Structured logging and basic health and metrics endpoints.
- **NFR-9 Portability**: Reproducible setup with containerized deployment and configuration through environment variables.
- **NFR-10 Legal and ethical**: Scraping respects robots.txt and rate limits and stores facts only. Source attribution is shown where required.

## 5. External Interfaces

- **Weather**: a free weather API (candidate: Open-Meteo).
- **Perfume data sources**: pluggable adapters (scraper, alternate source, manual entry).
- **Browser**: Geolocation API (with a manual city fallback).

## 6. Data Model Outline

- **User**: id, email, password hash, settings (rotation on or off), created at.
- **Perfume (catalog)**: id, name, brand, concentration, scent family, ingestion status, source, last fetched.
- **Note / Accord**: name, type; linked to perfumes (many to many; notes have a layer of top, middle or base).
- **PerfumeAttributes**: projection, longevity, strength, season scores, occasion scores, time-of-day scores.
- **CollectionItem**: user, perfume, personal overrides.
- **Occasion**: fixed lookup list.
- **WearLog**: user, perfume, date, occasion, weather snapshot, rating, note.

## 7. Out of Scope (v1)

Bottle size, purchase date and amount-left tracking. Free-text occasion input. Social features or sharing. Native mobile apps. Offline mode.

## 8. Future Enhancements

Free-text occasions, smarter learned recommendations from rating history, perfume layering suggestions, an admin moderation panel, a PWA install option.

## 9. Technology Stack and Tools

The system is a modular monolith: one Spring Boot backend split into feature modules (auth, catalog, collection, ingestion, recommendation, wearlog), plus a separate React frontend. Each part is deployed on its own.

### 9.1 Core stack

| Layer | Choice | Purpose |
| --- | --- | --- |
| Language | Java 25 (LTS) | Backend language |
| Backend framework | Spring Boot 4.x (Web, Data JPA, Security, Validation, Actuator) | REST API, data access, authentication, health endpoints |
| Build tool | Maven | Dependency and build management |
| Database | PostgreSQL | Users, shared catalog, collections, wear logs |
| Schema migrations | Flyway | Versioned database changes |
| Authentication | Spring Security with JWT in an HttpOnly cookie | Stateless login with XSS-safe token storage |
| Frontend | React with TypeScript (Vite) and Tailwind CSS | Responsive mobile and desktop interface |
| API contract | OpenAPI (springdoc) | Generated API documentation and Swagger UI |

### 9.2 Perfume data acquisition

The ingestion module tries these sources in order and stops at the first that succeeds:

1. The shared catalog: if the perfume already exists, link it to the user's collection.
2. A starter dataset, after its license has been checked.
3. A web scraper using Jsoup (Playwright only if a source needs JavaScript rendering), with Resilience4j for retries, rate limiting and circuit breaking. It respects robots.txt and stores facts only.
4. An optional AI lookup as a last resort. Results are labeled "AI-estimated", editable by the user, limited per user, and can be switched off.
5. Manual entry by the user.

Lookups run in the background and show a Loading, Ready or Failed status. Every result is cached in the catalog.

### 9.3 External services and hosting

| Need | Service | Notes |
| --- | --- | --- |
| Live weather | Open-Meteo | Free, no API key |
| Password-reset email | Brevo | Free plan |
| Frontend hosting | Vercel | Free tier |
| Backend hosting | Render | Free tier; the service sleeps when idle, so the first request after a quiet period is slow |
| Database hosting | Neon | Free PostgreSQL; sleeps when idle and wakes quickly |

Free-tier limits change, so they are rechecked before deployment.

### 9.4 Testing and quality

- Backend tests: JUnit 5, Mockito, AssertJ, and Testcontainers (a real PostgreSQL in a container).
- Frontend tests: Vitest and React Testing Library.
- Code quality: Spotless for formatting and JaCoCo for test coverage.
- Continuous integration: GitHub Actions builds and tests every push.

### 9.5 Development tools and practices

- Environment: Google Antigravity with VS Code, Git and GitHub.
- Docker: runs the local PostgreSQL through Docker Compose at first; the application container comes later.
- Practices: layered architecture (controller, service, repository), SOLID and design patterns (Strategy for scoring, Adapter for data sources), API-first design, configuration through environment variables, structured logging, and a short Architecture Decision Record for each major decision.

## 10. Open Items

- Specific sources for the starter dataset and the scraper (license and terms check), and whether to enable the optional AI lookup step.
