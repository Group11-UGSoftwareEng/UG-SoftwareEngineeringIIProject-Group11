# Runtime-only image
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy prebuilt JAR from CI output into the image
COPY target/Group11-expenses-tracker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Run the JAR
CMD ["java", "-jar", "app.jar"]
