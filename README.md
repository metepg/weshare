# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot (Java 17) / Angular 18 / Flyway / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.

---

# Usage

## Create bills

  ![Create bill](resources/videos/CreateBill.gif)

## Show bills

  ![Show bills](resources/videos/Bills.gif)

## Pay debt

  ![Pay debt](resources/videos/PayDebt.gif)

---

# Installation

## Prerequisites

- PostgreSQL (can be run locally or optionally in a Docker container)
- Java 17
- Maven
- NodeJS
- (Optional) Docker and Docker-compose

## Instructions for setting up local environment
### 1. Create database
- Create a local PostgreSQL database named "weshare"

**OR**

- Run the docker-compose.yml located at root of project.
- Change spring.datasource.url in dev.properties

---

### 2. Initialize Application

Follow these steps to set up the application.

1. Run `mvn clean install` in `/server` directory.
2. Start frontend with `npm start` in `/client` directory and Spring Boot with `mvn spring-boot:run -Dspring-boot.run.profiles=dev` in `/server` directory.
3. Populate database with 2 users and some demo data by running command `psql -U postgres -d weshare -f server/src/main/resources/db/create-test-data.sql` in <strong>PROJECT ROOT</strong>

After completing these steps, you can access the application at [http://localhost:8080](http://localhost:8080). 

---

### 3. Create Test Data (optional)
Depending on your setup, use the appropriate command **in project root** to create test data:

- **Using Local Database**:
  ```sh
  psql -U postgres -d weshare -f server/src/main/resources/db/create-test-data.sql
  ```

- **Using Container Database**:
  ```sh
  docker exec -i weshare_db psql -U postgres -d weshare -f /create-test-data.sql
  ```
---

### Default login credentials (if demo data has been added):
- **User 1:**
  - Username: `user`
  - Password: `password`
- **User 2:**
    - Username: `user2`
    - Password: `password`

---

### Resetting database

- **Using Local Database**:
1.   ```sh
     npm run resetLocalDB
     ```

- **Using Container Database**:
  ```sh
  docker-compose down -v && docker-compose up -d
  ```
 
---


## Extra
For production optimized .jar run `mvn clean package` in `/server` directory.

TODO:
- Support for more than 2 users
- Implement tests
