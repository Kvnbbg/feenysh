# Feenysh Messaging Backend

Feenysh is a Spring Boot application designed to serve as a backend for a real-time messaging service. It provides APIs for user authentication, registration, and will be extended for core messaging functionalities.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Building the Application](#building-the-application)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
- [Configuration](#configuration)
  - [Database](#database)
  - [JWT](#jwt)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)

## Features

*   User registration with username, email, and password.
*   User login with JWT-based authentication.
*   Secure password hashing using BCrypt.
*   Basic API structure for future messaging features.
*   In-memory H2 database for development (configurable for PostgreSQL).

## Prerequisites

*   Java Development Kit (JDK) 21 or later.
*   Apache Maven 3.6.x or later.
*   (Optional) PostgreSQL database for production/persistent storage.
*   (Optional) An API testing tool like Postman or curl.

## Setup and Installation

1.  **Clone the repository (if applicable):**
    ```bash
    # git clone <repository-url>
    # cd feenysh
    ```

2.  **Ensure JDK and Maven are installed and configured correctly.**

## Running the Application

You can run the application directly using the Spring Boot Maven plugin:

```bash
mvn spring-boot:run
```

The application will start by default on port `8080`.

## Building the Application

To build an executable JAR file:

```bash
mvn clean package
```

The JAR file will be created in the `target/` directory (e.g., `feenysh-1.0-SNAPSHOT.jar`). You can run it using:

```bash
java -jar target/feenysh-1.0-SNAPSHOT.jar
```

## API Endpoints

The base URL for the API is `http://localhost:8080/api`.

### Authentication

*   **Register User:**
    *   `POST /api/auth/register`
    *   **Request Body:**
        ```json
        {
            "username": "yourusername",
            "password": "yourpassword",
            "email": "user@example.com",
            "displayName": "Your Name"
        }
        ```
    *   **Success Response (201 Created):**
        ```json
        {
            "message": "User registered successfully! Please login."
        }
        ```
    *   **Error Response (400 Bad Request):** If username/email is taken or validation fails.
        ```json
        {
            "message": "Username is already taken!"
        }
        ```

*   **Login User:**
    *   `POST /api/auth/login`
    *   **Request Body:**
        ```json
        {
            "username": "yourusername",
            "password": "yourpassword"
        }
        ```
    *   **Success Response (200 OK):**
        ```json
        {
            "accessToken": "your_jwt_token",
            "tokenType": "Bearer",
            "userId": 1,
            "username": "yourusername"
        }
        ```
    *   **Error Response (401 Unauthorized):** If credentials are invalid.
        ```json
        {
            "message": "Login failed: Invalid username or password."
        }
        ```

## Configuration

Configuration is managed in `src/main/resources/application.properties`.

### Database

*   By default, an in-memory H2 database is used.
*   The H2 console is accessible at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:feenydb`, User: `sa`, Password: (blank)).
*   To switch to PostgreSQL:
    1.  Add the PostgreSQL dependency to `pom.xml`.
    2.  Update the datasource properties in `application.properties` (comment out H2, uncomment and configure PostgreSQL).

### JWT (JSON Web Token)

*   `app.jwtSecret`: The secret key for signing JWTs. **IMPORTANT: For production, use a strong, randomly generated secret and store it securely (e.g., environment variable). Do not use the default value.**
*   `app.jwtExpirationInMs`: JWT expiration time in milliseconds (default: 24 hours).

## Testing

To run the tests:

```bash
mvn test
```

Integration tests use an H2 in-memory database and will roll back transactions after each test.

## Future Enhancements

*   One-to-one real-time messaging via WebSockets.
*   Group chat functionality.
*   Message history and persistence.
*   User profiles and contact management.
*   End-to-end encryption (conceptual).
*   Deployment to cloud platforms.
*   Rate limiting on authentication endpoints.
*   More comprehensive API documentation (e.g., Swagger/OpenAPI).
```
