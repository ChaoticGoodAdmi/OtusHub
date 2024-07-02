FROM gradle:7.2.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 4242
ENTRYPOINT ["java", "-jar", "app.jar"]
