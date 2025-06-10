# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot (Java 21) / Angular 19 / Flyway / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.
- Using this repository to try out Testcontainers for integration tests and local development.

# Table of Contents

- [Installation](#installation)
- [Development](#development)
- [Resetting database](#resetting-database)
- [Features](#features)
- [Extra](#extra)
- [TODO](#todo)

# Installation

Prerequisites:

| Requirement     | Details                                                                   |
|-----------------|---------------------------------------------------------------------------|
| Java 21         | ✅ **Required**                                                            |
| Docker / Podman | ✅ **Required**                                                            |
| Maven           | 🛠️ Not needed if using the **Maven Wrapper** provided in the project     |
| Node.js         | 🛠️ Not needed if using the local **Node.js** installation in the project |
| PostgreSQL      | 🛠️ Not needed if using the basic setup                                   |


```sh
# Running this will install maven dependencies.
# Maven build process also installs node locally to this project and npm dependencies in client directory.
./mvnw compile
```

# Development

Requires Docker / Podman daemon to be running. Run these to start the app for development.

```sh
./mvnw -f server/pom.xml spring-boot:test-run
```
```sh
cd client && npm start
# IMPORTANT FOR WINDOWS USERS
# Before starting client you need to change paths in package.json
# from ./node/node to .\\node\\node etc..
```

Insert test data by finding testcontainer database with `docker ps -a`

Then run from project root `docker exec -i {container_name} psql -U test -d weshare < server/src/main/resources/db/create-test-data.sql`

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
