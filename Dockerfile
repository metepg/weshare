# ---------- frontend ----------
FROM node:lts-alpine AS client
WORKDIR /client

COPY client/package*.json ./
RUN --mount=type=cache,target=/root/.npm npm ci --no-audit --no-fund

COPY client/ .
RUN npm run build

# ---------- server ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY pom.xml ./
COPY server/pom.xml server/pom.xml

COPY server/ server/
COPY --from=client /client/dist client/dist/
RUN ./mvnw -q -pl server -DskipTests package

# ---------- runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN adduser --system appuser
USER appuser
COPY --from=build /app/server/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
