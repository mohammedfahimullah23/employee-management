# Use lightweight Java runtime
FROM eclipse-temurin:17-jre-alpine

# Create app directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
