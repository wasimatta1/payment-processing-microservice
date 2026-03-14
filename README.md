# Payment Processing Microservice

**Technology**: Grails 5.3.6  
**Persistence**: GORM with Oracle 21c XE  
**Objective**: Small REST API for payment transactions.

## How to Run the Project

1. **Prerequisites**:
   - Grails 5.3.6 installed.
   - Oracle 21c XE running (user: `paymentuser`, pass: `Payment123`, URL: `jdbc:oracle:thin:@localhost:1521/XEPDB1`).

2. **Local Run**:
   - Open terminal in project root.
   - Run `./gradlew bootRun`.
   - App runs at `http://localhost:8080`.
   - Swagger UI: `http://localhost:8080/swagger-ui.html`.

3. **Docker Run** :
   - Build JAR: `./gradlew bootJar`.
   - Build image: `docker build -t payment-service .`.
   - Run container: `docker run -p 8080:8080 --name payment-container -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@host.docker.internal:1521/XEPDB1 -e SPRING_DATASOURCE_USERNAME=paymentuser -e SPRING_DATASOURCE_PASSWORD=Payment123 payment-service`.
   - Access at `http://localhost:8080`.
  
 ## Example API Requests

All APIs return JSON. Use curl or Postman. X-API-KEY is required for payment-related APIs (get it from create merchant response).

### 1. Create Merchant (POST /api/merchants)
**curl**
curl -X POST http://localhost:8080/api/merchants 
-H "Content-Type: application/json" 
-d '{"name":"Test Store","email":"store@test.com"}'
text**Postman**
- Method: POST
- URL: `http://localhost:8080/api/merchants`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
{
"name": "Test Store",
"email": "store@test.com"
}
text- Response example:
{
"id": 1,
"name": "Test Store",
"email": "store@test.com",
"apiKey": "generated-api-key",
"active": true,
"dateCreated": "2026-03-14T00:00:00Z",
"lastUpdated": "2026-03-14T00:00:00Z"
}
text- Copy `apiKey` for other requests.

### 2. Create Payment (POST /api/payments)
**curl**
curl -X POST http://localhost:8080/api/payments 
-H "X-API-KEY: generated-api-key" 
-H "Content-Type: application/json" 
-d '{"reference":"INV-10001","amount":120.50,"currency":"USD","description":"Order payment"}'
text**Postman**
- Method: POST
- URL: `http://localhost:8080/api/payments`
- Headers: `X-API-KEY: generated-api-key`, `Content-Type: application/json`
- Body (raw JSON):
{
"reference": "INV-10001",
"amount": 120.50,
"currency": "USD",
"description": "Order payment"
}
text- Response example:
{
"id": 1,
"reference": "INV-10001",
"amount": 120.5,
"currency": "USD",
"description": "Order payment",
"status": "PENDING",
"merchant": {
"id": 1
},
"dateCreated": "2026-03-14T00:00:00Z",
"lastUpdated": "2026-03-14T00:00:00Z"
}
text### 3. Capture Payment (POST /api/payments/{reference}/capture)
**curl**
curl -X POST http://localhost:8080/api/payments/INV-10001/capture 
-H "X-API-KEY: generated-api-key"
text**Postman**
- Method: POST
- URL: `http://localhost:8080/api/payments/INV-10001/capture`
- Headers: `X-API-KEY: generated-api-key`
- Body: None

- Response example: Updated transaction with "status": "SUCCESS"

### 4. Refund Payment (POST /api/payments/{reference}/refund)
**curl**
curl -X POST http://localhost:8080/api/payments/INV-10001/refund 
-H "X-API-KEY: generated-api-key"
text**Postman**
- Method: POST
- URL: `http://localhost:8080/api/payments/INV-10001/refund`
- Headers: `X-API-KEY: generated-api-key`
- Body: None

- Response example: Updated transaction with "status": "REFUNDED"

### 5. Get Payment Details (GET /api/payments/{reference})
**curl**
curl -X GET http://localhost:8080/api/payments/INV-10001 
-H "X-API-KEY: generated-api-key"
text**Postman**
- Method: GET
- URL: `http://localhost:8080/api/payments/INV-10001`
- Headers: `X-API-KEY: generated-api-key`

- Response example: Full transaction JSON

### 6. List Merchant Payments (GET /api/payments)
**curl**
curl -X GET "http://localhost:8080/api/payments?status=SUCCESS&max=10&offset=0" 
-H "X-API-KEY: generated-api-key"
text**Postman**
- Method: GET
- URL: `http://localhost:8080/api/payments?status=SUCCESS&max=10&offset=0`
- Headers: `X-API-KEY: generated-api-key`

- Response example: Array of transactions

## Notes or Assumptions Made
- API key generated using UUID on merchant creation.
- Email and reference uniqueness enforced by GORM constraints and service checks.
- Status transitions checked in service layer with @Transactional for atomicity.
- Amount >0 validated in command object.
- Optional description allowed.
- Pagination implemented with GORM criteria (bonus).
- Command objects used for request validation (bonus).
- Basic integration tests cover endpoints (bonus).
- Dockerfile for deployment (bonus).
- Swagger for docs (bonus) at /swagger-ui.html.
- Oracle for persistence, but can switch to H2 by changing application.yml.
- Assume adult users, no moralizing.
- Factual, no deception.

**1. Create Merchant**
