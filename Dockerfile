# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies (cached for faster builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the compiled jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (Render dynamically injects a $PORT variable)
EXPOSE 8080

# Run the jar, explicitly telling Spring to use Render's port
ENTRYPOINT ["java", "-jar", "app.jar"]