# URL Shortener - Shortener Service

## Project goal

I created this project to practice building a modern microservice and learning how to integrate external systems into 
an application. The goal was to produce something that would be considered "production-ready" while using new technologies
and methodologies. This project utilizes a PostgreSQL database for persistence, a Kafka instance for handling messaging,
and a Kotlin microservice for handling both the incoming requests and the messages from Kafka.

The microservice architecture is built to allow easy splitting from a monolithic application to a group of services if
the need arose. The code is organized according to the principles defined in *Software Architecture: The Hard Parts* by
Ford et al.

## Prerequisites

- JDK 21+
- Gradle 9.x.x (Gradle wrapper is recommended)
- Docker 28.x.x (Docker compose 2.x.x)
- K6 1.x.x (for load tests)

## Running with Docker Compose

Use the compose file `compose.yaml` to set up external services. It will start the following services:

- PostgreSQL database on port 5432
- Kafka on ports:
  - 9092 (advertised listener)
  - 9997 (JMX endpoint)
- Kafka Schema Registry on port 8085
- Kafbat UI on port 8888
- Grafana on ports:
  - 3000 (Grafana UI)
  - 4137 (gRPC OpenTelemetry Collector)
  - 4138 (http/protobuf OpenTelemetry Collector)

Run compose with:

```bash
docker compose --file compose.yaml up --detach
```

Optionally, compose can build and run the service image using the `shortener` profile:

```bash
docker compose --file compose.taml --profile shortener up --detach
```

To stop and remove the stack:

```bash
docker compose --file compose.yaml down
```

## Build Docker image (manual)

```bash
docker build --tag shortener-service:latest .
```

Run the image with environment variables from `local.env`:

```bash
docker run --env-file local.env --publish 8080:8080 shortener-service:latest
```

## Running with Gradle (development)

Use the Gradle wrapper for local development. Build and run the service with:

```bash
./gradlew clean build
./gradlew bootRun
```

To produce a runnable jar and start it:

```bash
./gradlew bootJar && java -jar build/libs/shortener-service-0.0.1-SNAPSHOT.jar
```

## Load testing (k6)

Load test scripts live in the `k6/` directory. Run a standard test with:

```bash
k6 run k6/load_test.js
```

To test the maximum performance of the application, use:

```bash
k6 run k6/upper_bound_test.js
```

## Project structure

- `src`
  - `main`
    - `kotlin`
      - `com/behlers/shortener/service`
        - `analytics` - code relating to processing analytics and queries against analytics table
        - `router` - routing shortened urls
        - `shared` - contains code shared between other packages
        - `url` - CRUD operations for urls
    - `proto` - protobuf resources
    - `resources` - application resources
  - `test` - test classes and resources
    - `kotlin/com/behlers/shortener/service`
      - `integration` - contains end-to-end tests

## Environment

Edit `local.env` to configure environment variables used by Docker Compose and docker run. Keep secrets out of the
repository.
