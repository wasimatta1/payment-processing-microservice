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



## Example API Requests (curl)

**1. Create Merchant**
