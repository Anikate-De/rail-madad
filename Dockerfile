# Use a Maven image to build the application
FROM maven:3.9.9-eclipse-temurin-22 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml ./
RUN mvn -f pom.xml dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn -f pom.xml clean package -DskipTests

# Use an OpenJDK image to run the application
FROM openjdk:22-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
