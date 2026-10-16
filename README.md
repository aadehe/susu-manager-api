# Susu Manager API

A REST API for managing a rotating savings group (susu), where members contribute a fixed amount each week and the pooled contributions are paid to one member in a predefined rotation.

## Project Status

**Current stage:** Initial project setup.

The application is built with Kotlin and Ktor. Business rules, API endpoints, and automated test coverage will be developed incrementally.

## Technology Stack

- Kotlin
- Ktor
- Gradle Kotlin DSL
- Netty HTTP server
- Kotlinx Serialization for JSON

## Prerequisites

- JDK 21 or another JDK version supported by the project's Gradle and Kotlin configuration

## Build and Test

Run the automated tests:

```bash
./gradlew test
```

Build the project:

```bash
./gradlew build
```

Run the application:

```bash
./gradlew run
```

## Scope

The API will support member management, weekly contribution recording, week closure, payout rotation, and reporting. It will validate business rules and represent monetary values without floating-point rounding errors.

## Assumptions and Design Decisions

See [NOTES.md](NOTES.md) for documented assumptions, design decisions, and potential improvements as the project evolves.

## License

No license has been selected yet.