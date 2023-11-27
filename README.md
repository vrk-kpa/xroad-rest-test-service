# xroad-rest-test-service

A simple REST service to test X-Road connectivity.

## Dependencies
 * Java 17
 * Maven
 * Docker for local development


## Local development with standalone security server

Build the application JAR file and start the app and security server in docker compose:
```shell
mvn clean package
cd local-dev
docker-compose build
docker-compose up
```
