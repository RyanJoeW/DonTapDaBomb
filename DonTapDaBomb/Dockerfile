# build de jar
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# kopieer de pom.xml en dependencies eerst (snellere build)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# kopieer de rest en bouw
COPY src ./src
RUN mvn clean package -DskipTests

# run de app
FROM eclipse-temurin:21-jdk
WORKDIR /app

# kopieer de jar vanuit de build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]