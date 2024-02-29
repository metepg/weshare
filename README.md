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

- PostgreSQL (can be run in container)
- Java 17
- Maven
- NodeJS

## Instructions for setting up local environment
Create a database named "weshare" (default). If you prefer a different name, you'll need to change the database name in
  - application-dev.properties
  - application-setup.properties
After cloning this repo run these commands in `/client` directory

### Initialize Application
1. `npm install` (installs dependencies)
2. `npm run dbsetup`. This will insert 2 users to database with the credentials that are found in `application-setup.properties`. 
3. Start the app with `npm run bdev`
4. You should see the app load at [http://localhost:8080](http://localhost:8080). You can login with 2 users:
    - **User 1:**
        - Username: `user`
        - Password: `password`
    - **User 2:**
        - Username: `user2`
        - Password: `password`
---

### Create Test Data (optional)
Depending on your setup, use the appropriate command **in project root** to create test data:

- **Using Local Database**:
  ```sh
  sudo -u postgres psql -d weshare -f server/src/main/resources/db/create-test-data.sql
  ```
 
- **Using Docker Container Database**:
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

Current state of the app supports only 2 users and has hardcoded Finnish labels and text.
