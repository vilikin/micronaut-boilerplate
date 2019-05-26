# Micronaut Boilerplate

Simple microservice demonstrating how to set up simple reactive API, reactive HTTP client, reactive Postgres client, Swagger and basic configuration utilising Micronaut framework.

## Running service

1. `docker-compose up -d postgres`
1. `cp .env.sample .env` (and edit values if you'd like to)
1. `source .env`
1. `./gradlew run`

## Running tests

1. `docker-compose up -d posgtes`
1. `./gradlew test`

## Development in IntelliJ IDEA

As Micronaut does it's magic at compile time with annotation processing, there needs to be some special treatment at IDE side.

First of all, make sure that Build, Execution, Deployment -> Compiler -> Annotation Processor -> `Enable annotation processing` is ticked in Preferences.

### Run server

Create build configuration with following values:
1. Use classpath of module: `micronaut.server.micronaut-server.main`
1. Select `micronaut.server.Application` as Main class
1. Fill in environment variables
1. **Important part**: Add Gradle task `classes` as a before launch step.
1. Run it!

You can get most of the information pre-filled by just navigating to the Application class and selectin `Run` next to the main method. However, you still need to edit the build configuration and add Gradle task `classes` as before launch task. (step 4)

### Run tests

Easiest way is to use Gradle test runner, it worked out of the box with annotation processing:

1. Build, Execution, Deployment -> Build Tools -> Gradle -> Runner -> **Run tests using: Gradle Test Runner**
1. Now you can just simply right click on the test directory and select `Run tests in ...`

## Swagger / OpenAPI

The server exposes raw OpenAPI YML file, Swagger UI and ReDoc:

Raw YML: /swagger/micronaut-boilerplate-1.0.yml
Swagger UI: /swagger-ui/
ReDoc: /redoc/
