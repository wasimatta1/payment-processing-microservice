# Payment Processing Microservice

**Technology**: Grails 5.3.6  
**Persistence**: GORM with Oracle 21c XE  
**Objective**: Small REST API for payment transactions.

---

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Run locally](#run-locally)
3. [Run with Docker](#run-with-docker)
4. [API Endpoints & Examples](#api-endpoints--examples)
   - [Create Merchant](#1-create-merchant-post-apimerchants)
   - [Create Payment](#2-create-payment-post-apipayments)
   - [Capture Payment](#3-capture-payment-post-apipaymentsreferencapture)
   - [Refund Payment](#4-refund-payment-post-apipaymentsreferencerefund)
   - [Get Payment Details](#5-get-payment-details-get-apipaymentsreference)
   - [List Merchant Payments](#6-list-merchant-payments-get-apipayments)
5. [Notes & Assumptions](#notes--assumptions)

---

## Prerequisites
- Grails **5.3.6** installed.
- Oracle **21c XE** running and reachable.
  - Example connection (used by this project):
    - JDBC URL: `jdbc:oracle:thin:@localhost:1521/XEPDB1`
    - Username: `paymentuser`
    - Password: `Payment123`

> Adjust these values in `grails-app/conf/application.yml` (or via environment variables) for your environment.

---

## Run locally
1. Open a terminal in the project root.
2. Start the application:

```bash
./gradlew bootRun
```

3. App default URL: `http://localhost:8080`
4. Swagger UI (if enabled): `http://localhost:8080/swagger-ui.html`

---

## Run with Docker
1. Build JAR:

```bash
./gradlew bootJar
```

2. Build Docker image:

```bash
docker build -t payment-service .
```

3. Run container (example; when Oracle runs on host machine and you want container to reach it):

```bash
docker run -p 8080:8080 \
  --name payment-container \
  -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@host.docker.internal:1521/XEPDB1 \
  -e SPRING_DATASOURCE_USERNAME=paymentuser \
  -e SPRING_DATASOURCE_PASSWORD=Payment123 \
  payment-service
```

4. Access service at: `http://localhost:8080`

> Tip: On Linux, `host.docker.internal` may not be available; use the Docker host IP or run Oracle in a container on the same Docker network.

---

## API Endpoints & Examples
All APIs return JSON. Payment-related endpoints require header `X-API-KEY` (returned when creating a merchant).

### 1. Create Merchant (POST /api/merchants)
**Request (curl)**

```bash
curl -X POST "http://localhost:8080/api/merchants" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Store","email":"store@test.com"}'
```

**Request (Postman)**
- Method: `POST`
- URL: `http://localhost:8080/api/merchants`
- Headers: `Content-Type: application/json`
- Body (raw JSON):

```json
{
  "name": "Test Store",
  "email": "store@test.com"
}
```

**Response example**
```json
{
  "id": 1,
  "name": "Test Store",
  "email": "store@test.com",
  "apiKey": "generated-api-key",
  "active": true
}
```

> Save the returned `apiKey` for subsequent requests.

---

### 2. Create Payment (POST /api/payments)
**Request (curl)**

```bash
curl -X POST "http://localhost:8080/api/payments" \
  -H "X-API-KEY: generated-api-key" \
  -H "Content-Type: application/json" \
  -d '{"reference":"INV-10001","amount":120.50,"currency":"USD","description":"Order payment"}'
```

**Request (Postman)**
- Method: `POST`
- URL: `http://localhost:8080/api/payments`
- Headers: `X-API-KEY: generated-api-key`, `Content-Type: application/json`
- Body (raw JSON):

```json
{
  "reference": "INV-10001",
  "amount": 120.50,
  "currency": "USD",
  "description": "Order payment"
}
```

**Response example**
```json
{
  "id": 1,
  "reference": "INV-10001",
  "amount": 120.5,
  "currency": "USD",
  "description": "Order payment",
  "status": "PENDING",
  "merchant": { "id": 1 },
  "dateCreated": "2026-03-14T00:00:00Z",
  "lastUpdated": "2026-03-14T00:00:00Z"
}
```

---

### 3. Capture Payment (POST /api/payments/{reference}/capture)
**Request (curl)**

```bash
curl -X POST "http://localhost:8080/api/payments/INV-10001/capture" \
  -H "X-API-KEY: generated-api-key"
```

**Response**: Updated transaction object with `"status": "SUCCESS"` (example).

---

### 4. Refund Payment (POST /api/payments/{reference}/refund)
**Request (curl)**

```bash
curl -X POST "http://localhost:8080/api/payments/INV-10001/refund" \
  -H "X-API-KEY: generated-api-key"
```

**Response**: Updated transaction object with `"status": "REFUNDED"` (example).

---

### 5. Get Payment Details (GET /api/payments/{reference})
**Request (curl)**

```bash
curl -X GET "http://localhost:8080/api/payments/INV-10001" \
  -H "X-API-KEY: generated-api-key"
```

**Response**: Full transaction JSON object.

---

### 6. List Merchant Payments (GET /api/payments)
**Request (curl)**

```bash
curl -X GET "http://localhost:8080/api/payments?status=SUCCESS&max=10&offset=0" \
  -H "X-API-KEY: generated-api-key"
```

**Response**: Array of transaction objects (paginated if applicable).




