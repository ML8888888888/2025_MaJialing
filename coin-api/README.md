# Coin Change API - Dropwizard Application

This is a RESTful web service built using **Java Dropwizard** to calculate the minimum number of coins required to make up a target amount.

---

## Features

- Input a **target amount** and a list of available **coin denominations**
- Returns the **minimum number of coins** to reach the target amount
- Handles invalid inputs gracefully
- Dockerized for easy deployment
- Deployed on AWS EC2

---

## API Specification

### POST `/api/coin-change`

**Request JSON:**

```json
{
  "targetAmount": 7.03,
  "availableDenominations": [0.01, 0.5, 1, 5, 10]
}

```
---

## How to Build & Run with Docker
### 1.Clone this repo
```
git clone https://github.com/ML8888888888/2025_MaJialing.git
cd 2025_MaJialing/coin-api
```
### 2.Build the Docker image
```
docker build -t coin-change-service .
```
### 3.Run the Docker container
```
docker run -d -p 8080:8080 -p 8081:8081 coin-change-service
```
The service will start and be accessible at:
```
http://localhost:8080/api/coin-change
```
---

## Configuration

The application uses a Dropwizard config.yml file:
```
server:
  applicationConnectors:
    - type: http
      port: 8080
  adminConnectors:
    - type: http
      port: 8081
```
---

## Requirements

- Java 17

- Maven

- Docker

---

## Unit Tests

Unit tests are implemented using JUnit 5, covering:

- Valid combinations

- Invalid inputs

- Edge cases (0, 10000, impossible cases)

To run tests:
```
mvn test
```

---

## Health Check

To see your applications health enter url `http://localhost:8081/healthcheck`
