FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl feedback-api -am clean package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/feedback-api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]