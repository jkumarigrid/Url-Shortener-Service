
# URL Shortener

A simple URL shortener built using Java 17, Spring Boot, Spring Data JPA, and H2 Database.

## Features

- Shorten long URLs using unique short codes.
- Reuse the existing short code when the same URL is submitted again.
- Redirect short codes to their original URLs.
- Validate URLs and reject empty or invalid input.
- Return appropriate HTTP status codes for errors.
- Persist URL mappings using Spring Data JPA and H2.
- Interactive API documentation using Swagger UI and OpenAPI.
- Automated controller and service tests.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- Springdoc OpenAPI / Swagger UI
- JUnit 5 and Mockito

## Getting Started

### Prerequisites

- Java 17 or later
- Maven, or use the included Maven Wrapper

### Run the Application

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application starts at `http://localhost:8080`.

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

Swagger UI lets you explore the available endpoints, provide request data, and execute API calls directly from your browser.

## API Endpoints

### 1. Shorten a URL

**Endpoint:** `POST /api/urls`

Request:

```json
{
  "url": "https://www.google.com"
}
```

Successful response (`200 OK`):

```json
{
  "originalUrl": "https://www.google.com",
  "shortCode": "abc12345"
}
```

The generated short code will vary.

Submitting the same URL again returns its existing short code instead of creating another mapping.

**Errors:**
- `400 Bad Request` if the URL is empty or invalid.

### 2. Redirect to the Original URL

**Endpoint:** `GET /{shortCode}`

Example:

```bash
curl -i http://localhost:8080/abc12345
```

A successful lookup returns a `302 Found` redirect with a `Location` header containing the original URL.

**Errors:**
- `404 Not Found` if the short code does not exist.

## Running Tests

Run all tests with:

```bash
./mvnw clean test
```

On Windows:

```bash
mvnw.cmd clean test
```

## Database

The application uses an H2 in-memory database. URL mappings are stored while the application is running and are lost when the database shuts down.