FROM openjdk:17-jdk-slim
WORKDIR /app
COPY gradlew .
COPY build.gradle.kts .
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew
RUN ./gradlew build
COPY build/libs/OtusHub.jar /app/OtusHub.jar
ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=
ENV jwt.secret=
ENV SERVICE_URL=
ENV JAVA_OPTS=""
CMD ["sh", "-c", "java $JAVA_OPTS -jar OtusHub.jar"]
EXPOSE 4242