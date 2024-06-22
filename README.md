# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot / Angular 18 / PostgreSQL.
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
- Docker and Docker-compose (optional)
- Java 17
- Maven
- NodeJS

## Instructions for setting up local environment
### 1. Create database
- Create a local PostgreSQL database named "weshare" (default)

**OR**

- Run the docker-compose.yml located at root of project.

If you prefer a different database name, you'll need to change the database name in:
- `application-dev.properties`
- `application-setup.properties`
- `docker-compose.yml` (if using container)

---

### 2. Initialize Application

Follow these steps to set up the application. Run the commands in `/client` directory

1. Run `npm install` to install dependencies.
2. Execute `npm run dbsetup` to populate the database with initial data, including inserting 2 users as defined in `application-setup.properties`.
3. Start frontend with `npm start` and Spring Boot with `mvn spring-boot:run -Dspring-boot.run.profiles=dev` or just both with your IDE.

After completing these steps, you can access the application at [http://localhost:8080](http://localhost:8080). 

Default login credentials:
- **User 1:**
  - Username: `user`
  - Password: `password`
- **User 2:**
    - Username: `user2`
    - Password: `password`

**IMPORTANT:** If you reset the database at any point, you must repeat the setup process starting from step 2 to reinitialize the database and reinsert the users.

---

### Resetting database

- **Using Local Database**:
  ```sh
  npm run resetLocalDB
  ```

- **Using Container Database**:
  ```sh
  docker-compose down -v && docker-compose up -d
  ```
 
---

### Create Test Data (optional)
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

## Extra
Starting the app with `npm run bdev` inside `/client` directory does 3 things

1. Builds Angular files to Spring folder `src/main/resources/static`
2. Runs `mvn clean package` that builds the .jar file 
3. Runs .jar file with profile=dev (for spring boot).

For production optimized .jar run `npm run bprod` in `client` directory.

TODO:
- Support for more than 2 users
- Implement tests
