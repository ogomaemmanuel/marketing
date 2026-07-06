# Marketing

A modular Java-based marketing application. This repository contains multiple modules (core, api, infrastructure) organized as a Maven multi-module project. The project uses the Maven wrapper for reproducible builds and includes helper scripts for running with Docker.

## What this repo contains
- core/ — core domain, abstractions, and implementations
- api/ — HTTP/API layer (controllers, adapters)
- infrastructure/ — infra adapters, deployment and infra-as-code
- docker-compose-run.sh, compose.yaml — helper scripts to run the system locally
- pom.xml, mvnw, mvnw.cmd — Maven wrapper and parent POM

## Prerequisites
- Java 17+ (or the version declared in pom.xml)
- Maven (optional if using the included Maven wrapper)
- Docker & Docker Compose (if you plan to run via docker-compose-run.sh)

## Build
From the repository root:

Linux / macOS

```bash
./mvnw clean package -DskipTests
```

Windows (Powershell / CMD)

```powershell
mvnw.cmd clean package -DskipTests
```

To run tests:

```bash
./mvnw test
```

## Run (local, with Docker)
The repo includes a helper script to run the system with Docker Compose. Adjust environment variables in an `.env` file (do not commit secrets).

```bash
./docker-compose-run.sh
```

## Project structure & architecture
The codebase follows a layered/hexagonal style with packages such as:

- com.ogoma.marketing.core.domain — domain objects and entities
- com.ogoma.marketing.core.application — application services and orchestration
- com.ogoma.marketing.core.abstractions — interfaces and ports
- com.ogoma.marketing.core.implementations — concrete adapters/helpers

This separation makes it easier to test and replace infrastructure adapters.

## Contributing
Please read CONTRIBUTING.md for how to report issues, propose changes, branch/PR conventions, and testing expectations.

## License
No license is set yet. If you want to publish or share this repository, add a LICENSE file at the repository root and include license metadata in `pom.xml`.

## Security
- If a local `.env` file exists, ensure it does not contain secrets before committing. Use `.env.example` to share non-sensitive defaults.

## Contact / Maintainers
- Repository owner: ogomaemmanuel

