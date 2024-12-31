# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot (Java 21) / Angular 18 / Flyway / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.
- Using this repository to try out Testcontainers for integration testing.

# Table of Contents

- [Installation](#installation)
- [Start the app](#start-the-app)
  - [Recommended way](#recommended-way)
  - [Testcontainers way](#testcontainers-way)
  - [Local database way](#local-database-way)
- [Resetting database](#resetting-database)
- [Features](#features)
  - [Create bills](#create-bills)
  - [Show bills](#show-bills)
  - [Pay debt](#pay-debt)
- [Extra](#extra)
- [TODO](#todo)

# Installation

Prerequisites:

- PostgreSQL (can be run in a Docker container or optionally with local database installation)
- Java 21
- Maven
- NodeJS
- Docker and docker-compose

---
Run `mvn clean install` in `/server` directory. This will also install node and npm dependencies in `/client` directory.

Users to login with after the application is running:
- **User 1:**
  - Username: `user`
  - Password: `password`
- **User 2:**
  - Username: `user2`
  - Password: `password`

# Start the app

## Recommended way

- Run `docker-compose up -d` in project root. This will start containerized database.
- Run `mvn spring-boot:run -Dspring-boot.run.profiles=dev` in `/server` directory. This will start Spring Boot.
- Run `npm start` in `/client` directory. This will start Angular.
- Navigate to http://localhost:8080

## Testcontainers way

- Run `mvn spring-boot:test-run` in `server` directory
- Run `npm start` in `/client` directory. This will start Angular.
- Navigate to http://localhost:8080

## Local database way

- Create database with the name `weshare`
- Change `spring.datasource.url` in `application-dev.properties` to the port your database instance is running on.
- Run `mvn spring-boot:run -Dspring-boot.run.profiles=dev` in `/server` directory. This will start Spring Boot.
- Run `npm start` in `/client` directory. This will start Angular.
- Insert test data with users
  ```sh
  psql -U postgres -d weshare -f server/src/main/resources/db/create-test-data.sql
  ```
- Navigate to http://localhost:8080

# Resetting database

- **Using Local Database**:
1.   ```sh
     npm run resetLocalDB
     ```

- **Using Container Database**:
  ```sh
  docker-compose down -v && docker-compose up -d
  ```
 
# Features

## Create bills

![Create bill](resources/videos/CreateBill.gif)

## Show bills

![Show bills](resources/videos/Bills.gif)

## Pay debt

![Pay debt](resources/videos/PayDebt.gif)

# Extra

For production optimized .jar run `mvn clean package` in `/server` directory.

# TODO:
- Add GIFs for all implemented features
- Support for more than 2 users
- Implement tests
- Internationalization and localization

---
