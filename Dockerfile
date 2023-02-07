FROM maven:3-openjdk-18-slim AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package
FROM  openjdk:17
COPY --from=builder /app/target/*.jar /card2brain.jar
ENTRYPOINT ["java","-jar","/card2brain.jar"]
