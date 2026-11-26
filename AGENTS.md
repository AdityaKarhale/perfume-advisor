# Perfume Advisor: working rules
Read docs/SRS.md first. It is the source of truth for scope and the tech stack.

Project: public multi-user web app that recommends the top 3 perfumes from a user's collection for an occasion. Monorepo: /backend (Java 25 LTS, latest stable Spring Boot 4.x, Maven, PostgreSQL, Flyway), /frontend (React + TypeScript, added in M1), /docs.

The developer is a final-year CS student who wants to LEARN, not just receive code. So:
1. Work in small steps. After each step, stop, summarize what changed, and wait for my review.
2. For every file you create or change, explain in plain language what it does and why it is needed.
3. Do not add any dependency, tool, or feature I did not ask for. If you think one is needed, propose it and wait.
4. Spring Boot 4 differs from many tutorials. Verify APIs, starters and config against the current official docs. Do not rely on memory. If something is unclear, say so.
5. Every feature comes with tests. Use package-by-feature under com.perfumeadvisor.<module> and the layers controller, service, repository.
6. Never commit secrets. Configuration comes from environment variables.
7. Use short-lived feature branches, Conventional Commits, and pull requests, even though I work alone.
8. Show me any terminal command before you run it.