FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY build/libs/payment-service1-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]