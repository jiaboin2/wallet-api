# Introduction
The Wallet Api is a microservice containing the business logic for common
functions throughout the application, such as reading and interpreting QR codes.



# Main Features

Read and interpret QR codes.

# Build and Test
We have 2 different ways to build and test the project depending on the selected Spring Boot profile.
- `test` profile: This profile is used for unit testing. It uses an in-memory database and does not require any external dependencies.
- `local` profile: This profile is used for local development. It uses an in-memory database and generates default data to test the application.
- `local-docker` profile: This profile is used for local development. It uses a dockerized database and generates default data to test the application.

# Documentation

- Swagger: http://localhost:8088/swagger-ui.html
- OpenAPI: http://localhost:8088/api-docs

# Getting Started
## Prerequisites

- Java 17
- Gradle
- Spring Boot
- Docker Desktop
- IntelliJ IDEA
- Git