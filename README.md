# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot (Java 21) / Angular 18 / Flyway / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.
- Using this repository to try out Testcontainers for integration tests and local development.

# Table of Contents

- [Installation](#installation)
- [Development](#development)
- [Resetting database](#resetting-database)
- [Features](#features)
  - [Create bills](#create-bills)
  - [Show added bills](#show-added-bills)
  - [Pay debt](#pay-debt)
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
cd server && ./mvnw install
```

# Development

Requires Docker / Podman daemon to be running. Run these to start the app for development.

```sh
cd server && ./mvnw spring-boot:test-run
```
```sh
cd client && npm start
```
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

# Features

These features have been implemented so far:

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

For production optimized .jar run `./mvnw clean package` in `server` directory.

# TODO:
- Add GIFs for all implemented features
- Support for more than 2 users
- Implement more tests
- Internationalization and localization

---
