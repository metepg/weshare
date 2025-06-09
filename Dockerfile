FROM eclipse-temurin:21-jre

# Create non-root user
RUN adduser --system appuser

WORKDIR /app

# Copy the Spring Boot JAR
COPY server/target/*.jar app.jar

# Create logs directory and set ownership
RUN mkdir -p logs && chown -R appuser /app

# Switch to non-root user
USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.config.location=file:/config/"]
