# ---------- build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# ---------- runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN adduser --system appuser
USER appuser

COPY --from=build /app/server/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
