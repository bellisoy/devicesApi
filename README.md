# Devices API

A simple Spring Boot REST API for managing devices with full CRUD operations, filtering, and Swagger documentation. The
project is containerized using Docker for easy setup and execution.

---

## 🚀 Getting Started

### Prerequisites

- Docker
- Docker Compose

### Run the project

Clone the repository and start the application:

```bash
docker compose up --build
```

## 🌐 Access the application

Once the application is running, you can access:

- API base URL: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html#/

---

## 📦 Features

- Create, read, update, and delete devices
- Filter devices by brand
- Filter devices by state
- PostgreSQL database integration
- Dockerized setup
- Swagger / OpenAPI documentation
- Global exception handling

---

## 🧪 Testing the API

You can test the API using:

- Swagger UI (recommended)
- Postman
- curl

### Example endpoints

- `POST /devices`
- `GET /devices`
- `GET /devices/{id}`
- `PATCH /devices/{id}`
- `DELETE /devices/{id}`
- `GET /devices/brand/{brand}`
- `GET /devices/state/{state}`

---

## 🛠️ Future improvements

- Logging
- Spring Security (authentication & authorization)
- Use mapstruct for mapper
- Switch to query parameters for scalability (fetching devices)
- Publish Docker image to Docker Hub
- Caching and pagination
- API versioning and validation
- Database migrations (Flyway)
- Spring Boot Actuator monitoring