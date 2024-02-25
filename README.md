# Weshare

- App for splitting bills between 2 users.
- Built with Spring Boot / Angular 16 / PostgreSQL.
- Runs in browser and is styled just enough to look good with my current phone.
- I built this because MobilePay discontinued their own app Weshare at the start of 2023. This app is intended for my personal use only as it is hardcoded for 2 people but feel free to do whatever you want with it.

---

# Usage

## Create bills

- Set amount
- Set own amount of the bill
- Write description
- Set category
- Save

  ![Create bill](resources/videos/CreateBill.gif)

## Show bills

- Scroll added bills from last 6 months
- Look statistics about bill amounts from bar chart

  ![Show bills](resources/videos/Bills.gif)

## Pay debt

- Pay accumulated debt

  ![Pay debt](resources/videos/PayDebt.gif)

---

# Installation

## Prequisites

- PostgreSQL
- Java 17
- Maven
- NodeJS

## Instructions for setting up local environment

1. Clone repository normally
2. Create database `weshare` with schema `weshare`
3. `npm install` in `/client` directory
4. `mvn install` in `/server` directory 
5. Before starting the app go to `Application.java` and run the commented out code instead of the current code. Be sure to run that code with **DEV** profile This will insert 2 users to database with the credentials that are found in `application-dev.properties`. 
6. Undo changes in `Application.java` and start the app with `npm run bdev`
7. You should see the app load in <code>http://localhost:8080</code>. You can login with
    - username: `user`
    - password: `password`
  <br>

## Extra
Starting the app with `npm run bdev` inside `/client` directory does 3 things

1. Builds Angular files to Spring folder `src/main/resources/static`
2. Runs `mvn clean package` that builds the .jar file 
3. Runs .jar file with profile=dev (for spring boot).

For production optimized .jar run `npm run bprod` in `client` directory.
