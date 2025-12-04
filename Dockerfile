FROM openjdk:21-ea-slim-bookworm AS build

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve
RUN ./mvnw dependency:resolve-plugins
RUN ./mvnw dependency:go-offline 
COPY src ./src

RUN ./mvnw package -DskipTests

FROM openjdk:21-ea-slim-bookworm

WORKDIR /app

COPY --from=build /app/target/*.jar ./accounting-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "accounting-service.jar"]