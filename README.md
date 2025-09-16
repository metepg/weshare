# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot 4.x (Java 21) / Angular 20 / Flyway / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.
- Using this repository to try out stuff for my own development.

# Table of Contents

- [Installation](#installation)
- [Development](#development)
- [Resetting database](#resetting-database)
- [Features](#features)
- [Extra](#extra)
- [TODO](#todo)

# Installation

Prerequisites:

| Requirement     | Details                                                           |
|-----------------|-------------------------------------------------------------------|
| Java 21         | **Required**                                                      |
| Docker / Podman | **Required**                                                      |
| Node.js         | **Required**                                                      |
| Maven           | Not needed if using the **Maven Wrapper** provided in the project |
| PostgreSQL      | Not needed if using the basic setup                               |


```sh
# 1. Running this from project root will install maven dependencies.
./mvnw dependency:resolve
```

```sh
# 2. Running this from /client directory will install npm dependencies.
npm install
```

# Development

Requires Docker / Podman daemon to be running.

Run these to start the app for development.

`project root`:
```sh
./mvnw -f server/pom.xml spring-boot:run -Dspring-boot.run.profiles=dev
```

`client` directory
```sh
npm start
```

Insert users and test data by running this from project root `docker exec -i weshare_db psql -U postgres -d weshare < server/src/main/resources/db/create-test-data.sql`

Navigate to http://localhost:8080

Test users:
- **User 1:**
  - Username: `user`
  - Password: `password`
- **User 2:**
  - Username: `user2`
  - Password: `password`

# Resetting database

- Delete the container with normal Docker commands
- Restart app
- Add test data

# Features

Check [Weshare -> Wiki -> Features](https://github.com/metepg/weshare/wiki/Features) for all implemented features.

# Extra

- For production optimized .jar run `./mvnw clean package` in project root
- This project also has a Dockerfile, so it can be deployed as a container

# TODO:
- Add GIFs for all implemented features
- Support for more than two users
- Internationalization and localization

---
