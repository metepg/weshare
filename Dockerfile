FROM eclipse-temurin:21-jre

# Create non-root user and logs directory
RUN adduser --system appuser && \
    mkdir -p /app/logs && \
    chown appuser /app/logs

WORKDIR /app

# Copy the Spring Boot JAR
COPY server/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Start the app with external config support
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/config/"]
