# Base openjdk:17
FROM openjdk:17

# Add X-Road Test Service war to container
ADD target/rest-test-service-*.jar test-service.jar

# Entry with exec
ENTRYPOINT exec java $JAVA_OPTS -jar /test-service.jar

# Expose default port
EXPOSE 8080
