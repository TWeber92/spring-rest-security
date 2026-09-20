# Spring REST Security

Using Spring Boot, Spring Data JPA, and MySQL, this project demonstrates layered REST architecture with role-based security, AOP-based logging, and bean validation.

## Tech Stack

- Java 17
- Spring Boot 3.5.16 (Web, Data JPA, Security, Validation, AOP)
- Hibernate / JPA
- MySQL 8
- Log4j2
- Maven
- JUnit 5 + Mockito
- Spring Security Test

## Features

- Full CRUD operations for customers
- Role-based access control (ADMIN, USER) via in-memory authentication
- HTTP Basic authentication
- Bean validation on request bodies and path variables
- AOP-based exception logging
- Centralized exception handling with externalized error messages
- Constructor injection throughout

## Getting Started

### 1. Set up the database

Run `src/main/resources/tableScript.sql` against MySQL to create the `customer_db` schema and seed data.

### 2. Configure the connection

Update `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/customer_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
server.port=8765
```

### 3. Run

```powershell
.\mvnw spring-boot:run
```

The API will be available at `http://localhost:8765`.

## Authentication

In-memory users configured in `SecurityConfig`:

| Username | Password | Role |
|---|---|---|
| `smith` | `smith123` | ADMIN |
| `tim` | `tim123` | USER |

All requests require HTTP Basic authentication.

## Access Rules

| Path | Method | Required Role |
|---|---|---|
| `/infybank/customers` | GET, POST | ADMIN or USER |
| `/infybank/customer/**` | GET, PUT, DELETE | ADMIN |

## API Endpoints

| Method | Path | Description | Role |
|---|---|---|---|
| GET | `/infybank/customers` | List all customers | ADMIN, USER |
| POST | `/infybank/customers` | Add a new customer | ADMIN, USER |
| GET | `/infybank/customer/{customerId}` | Get a customer by ID | ADMIN |
| PUT | `/infybank/customer/{customerId}` | Update a customer's email | ADMIN |
| DELETE | `/infybank/customer/{customerId}` | Delete a customer | ADMIN |

### Example — get all customers

```
GET /infybank/customers
Authorization: Basic <base64 of smith:smith123>
```

Response:

```json
[
  {
    "customerId": 1,
    "emailId": "charles@example.com",
    "name": "Charles Smith",
    "dateOfBirth": "1985-04-12"
  }
]
```

### Example — update a customer's email

```
PUT /infybank/customer/1
Authorization: Basic <base64 of smith:smith123>
Content-Type: application/json
```

Request:

```json
{
  "emailId": "newemail@example.com"
}
```

Response (200):

```
Customer emailid successfully updated.
```

## Running Tests

```powershell
.\mvnw test
```

**Note:** `DemoSpringRestSecurityApplicationTests.contextLoads` requires a running MySQL instance with the `customer_db` schema. The service-layer unit tests use mocks and do not require a database.