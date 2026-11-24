# Perfume Advisor

Perfume Advisor is a public, multi-user web application that helps users build a personal perfume collection and recommends the **top 3 perfumes** from their collection for a chosen occasion, factoring in live weather, season, time of day, and past wear ratings.

For full requirements and scope, see [docs/SRS.md](docs/SRS.md).

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Java 25 (LTS) |
| Backend | Spring Boot 4.0.10 (Web, Validation, Actuator, Data JPA) |
| Build Tool | Maven (with Maven Wrapper `./mvnw`) |
| Database | PostgreSQL 17 |
| Schema Migrations | Flyway |
| Testing | JUnit 5, Mockito, AssertJ, Spring MockMvc, Testcontainers |
| Local Containers | Docker Compose |
| CI | GitHub Actions |
| Frontend | React + TypeScript (planned for M1) |

## Prerequisites

- **Java 25 (LTS)** (e.g., Eclipse Temurin 25)
- **Docker Desktop** (for PostgreSQL locally and Testcontainers in integration tests)
- **Git**

## Running Locally

1. **Clone the repository:**

   ```bash
   git clone https://github.com/AdityaKarhale/perfume-advisor.git
   cd perfume-advisor
   ```

2. **Create your local `.env` file:**

   Copy `.env.example` to `.env` and fill in your local database credentials (never commit `.env`):

   ```bash
   cp .env.example .env
   ```

3. **Start PostgreSQL with Docker Compose:**

   ```bash
   docker compose up -d
   ```

   Wait until the `postgres` container reports healthy (`docker compose ps`).

4. **Run the backend (with the `dev` profile):**

   Load the environment variables from `.env` into your shell (or configure them in your IDE run configuration), then start Spring Boot from `/backend`:

   - **PowerShell:**
     ```powershell
     Get-Content .env | ForEach-Object {
       if ($_ -match '^([^#=]+)=(.*)$') {
         [Environment]::SetEnvironmentVariable($matches[1].Trim(), $matches[2].Trim(), 'Process')
       }
     }
     cd backend
     .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
     ```
   - **macOS / Linux (bash/zsh):**
     ```bash
     set -a && source .env && set +a
     cd backend
     ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
     ```

5. **Verify the API is running:**

   Visit `http://localhost:8080/api/v1/ping` (or `http://localhost:8080/actuator/health`).

## Running Tests

Tests use JUnit 5, MockMvc, and Testcontainers (which spins up a real PostgreSQL 17 container automatically — make sure Docker is running):

```bash
cd backend
./mvnw verify
```

On Windows PowerShell, use `.\mvnw.cmd verify`.

## Project Structure

```text
perfume-advisor/
├── .github/workflows/   # GitHub Actions CI workflows
├── backend/             # Spring Boot 4 backend (Maven)
├── docs/
│   ├── SRS.md           # Software Requirements Specification
│   └── adr/             # Architecture Decision Records
├── docker-compose.yml   # Local PostgreSQL service
└── .env.example         # Template for local environment variables
```
