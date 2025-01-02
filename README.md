# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot (Java 21) / Angular 18 / Flyway / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.
- Using this repository to try out Testcontainers for integration tests and local development.

# Table of Contents

- [Installation](#installation)
- [Start the app](#start-the-app)
  - [1. Recommended way (Testcontainers)](#1-recommended-way-testcontainers)
  - [2. Normal way with containerized PostgreSQL](#2-normal-way-with-containerized-postgresql)
  - [3. Local database way](#3-local-database-way)
- [Resetting database](#resetting-database)
- [Features](#features)
  - [Create bills](#create-bills)
  - [Show added bills](#show-added-bills)
  - [Pay debt](#pay-debt)
- [Extra](#extra)
- [TODO](#todo)

# Installation

Prerequisites:

- PostgreSQL (can be run in a Docker container or optionally with local database installation)
- Java 21
- Maven
- NodeJS
- Docker / Podman

---
Run `mvn clean install` in `/server` directory. This will also install node and npm dependencies in `/client` directory.

# Start the app

Users to login with after the application is running:
- **User 1:**
  - Username: `user`
  - Password: `password`
- **User 2:**
  - Username: `user2`
  - Password: `password`

## The app can be started in three ways:

### 1. Recommended way (Testcontainers)

---
This is the easiest setup, requiring only Docker-daemon to be running. It will start Spring Boot & PostgreSQL in a container.

- Run `mvn spring-boot:test-run` in `/server` directory
- Run `npm start` in `/client` directory.
- Navigate to http://localhost:8080

### 2. Normal way with containerized PostgreSQL

---
Normal way with local Spring Boot, Angular and containerized database.

- Run `docker-compose up -d` in project root.
- Run `mvn spring-boot:run -Dspring-boot.run.profiles=dev` in `/server` directory.
```sh
# Insert test data
docker exec -i weshare_db psql -U postgres -d weshare -f /create-test-data.sql
```
- Run `npm start` in `/client` directory.
- Navigate to http://localhost:8080

### 3. Local database way

---
This is same as 'Normal way' but with local installation of PostgreSQL

- Create database with the name `weshare`
- Change `spring.datasource.url` in `application-dev.properties` to the port your database instance is running on.
- Run `mvn spring-boot:run -Dspring-boot.run.profiles=dev` in `/server` directory.
```sh
# Insert test data
psql -U postgres -d weshare -f server/src/main/resources/db/create-test-data.sql
```
- Run `npm start` in `/client` directory. This will start Angular.
- Navigate to http://localhost:8080

# Resetting database

## Using Testcontainers:
  1. Set `reuse-database=false` in `application-local.properties`
  2. Restart
  3. Set `reuse-database=true` in `application-local.properties`
  4. Restart

## Using Container Database:
```sh
docker-compose down -v && docker-compose up -d 
```
```sh
cd server && mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
```sh
docker exec -i weshare_db psql -U postgres -d weshare -f /create-test-data.sql
```

## Using Local Database:
```sh
cd client && npm run resetLocalDB
```
```sh
cd server && mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
```sh
psql -U postgres -d weshare -f server/src/main/resources/db/create-test-data.sql
```
---

# Features

Six features have been implemented, with their implementation and preview statuses outlined below:

| Feature          | Implemented | Preview Recorded |
|------------------|-------------|------------------|
| Create bills     | ✅           | ✅                |
| Show added bills | ✅           | ✅                |
| Pay debt         | ✅           | ✅                |
| Edit bills       | ✅           | ❌                |
| Delete bills     | ✅           | ❌                |
| Show statistics  | ✅           | ❌                |

## Create bills

![Create bill](resources/videos/CreateBill.gif)

## Show added bills

![Show bills](resources/videos/Bills.gif)

## Pay debt

![Pay debt](resources/videos/PayDebt.gif)

# Extra

For production optimized .jar run `mvn clean package` in `/server` directory.

# TODO:
- Add GIFs for all implemented features
- Support for more than 2 users
- Implement more tests
- Internationalization and localization

---
