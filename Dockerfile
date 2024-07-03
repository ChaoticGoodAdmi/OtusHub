FROM openjdk:17-jdk-slim
WORKDIR /app

COPY gradlew .
COPY gradlew.bat .
RUN chmod +x ./gradlew

COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src ./src

RUN ./gradlew clean build
RUN cp $(find /app/build -name "OtusHub.jar" | head -n 1) OtusHub.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/otushub
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV JWT_SECRET=eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJLaXIgVXNoYWtvdnYiLCJVc2VybmFtZSI6InVzZXItc2VydmljZSIsImV4cCI6MTcxOTI5ODk0NCwiaWF0IjoxNzE5Mjk4OTQ0fQ.kUPsL0suGFf_7zDyfv1X3kY4ps5UT_m7-UiXjFrmOMA
ENV SERVICE_URL=http://localhost:4242
ENV JAVA_OPTS=""

CMD ["sh", "-c", "java $JAVA_OPTS -jar OtusHub.jar"]
EXPOSE 4242