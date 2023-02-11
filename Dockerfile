FROM maven:3.8.6-amazoncorretto-17 AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package
FROM arm64v8/alpine:3.14
COPY --from=builder /app/target/*.jar /card2brain.jar
ENTRYPOINT ["java","-jar","/card2brain.jar"]
