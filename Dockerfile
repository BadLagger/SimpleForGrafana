# Stage 1: Build with Maven and JDK 25
FROM eclipse-temurin:25.0.1_8-jdk-jammy AS builder

ARG GIT_REPO

RUN apt-get update && apt-get install -y git maven

WORKDIR /build
RUN git clone ${GIT_REPO} .

RUN mvn clean package

# Stage 2: Runtime with JRE 25
FROM eclipse-temurin:25.0.1_8-jre-jammy

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

# Non-root пользователь для безопасности
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

EXPOSE 9991
ENTRYPOINT ["java", "-jar", "app.jar"]