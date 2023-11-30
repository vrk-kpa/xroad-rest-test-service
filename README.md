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

Then you can test calling the rest-test-service directly, without the security server:
```shell
curl -X GET localhost:8070/random
```

It takes a while for the security server to start up. You can check the logs with `docker compose logs -f ss`.

Login to the security server UI by opening http://localhost:4000 in your browser. 
The username is `xrd` and the password is `secret`.

Standalone security server has one X-Road service and one client, 
named `CS:ORG:1111:TestService` and `CS:ORG:1111:TestClient` respectively.
Add the rest-test-service to the service list of `CS:ORG:1111:TestService` as a REST service.
Service base URL in docker network is `http://test-service:8080`. When adding the service you can provide the service's
OpenApi description URL: `http://test-service:8080/v3/api-docs`. Security server will then automatically add all 
the service endpoints when creating the X-Road service. Set the service-code to `rest-test` and remember to enable the 
service.

In `CS:ORG:1111:TestService` service client settings, add a perimission for subject `CS:ORG:1111:TestClient` 
to make calls to the service code `rest-test`.
For detailed example of standalone security server configuration, refer to 
[the tutorial](https://github.com/digitaliceland/Straumurinn/blob/master/DOC/Manuals/standalone_security_server_tutorial.md).

After the config you can call the rest-test-service through the security server:
```shell
curl \
  -X GET \
  -H 'X-Road-Client: CS/ORG/1111/TestClient' \
  -i 'http://localhost:8080/r1/CS/ORG/1111/TestService/rest-test/random'
```
