FROM gradle:8.7-jdk17 AS build

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew bootJar -x test

FROM eclipse-temurin:17-jre

WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends libheif-examples \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/build/libs/*.jar app.jar
RUN mkdir -p /app/uploads
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
