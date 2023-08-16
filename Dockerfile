# Use the official OpenJDK base image for Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/dragonfly-0.0.1-SNAPSHOT.jar /app/app.jar

COPY temp/ /app/temp/

# Expose the port that the application will listen on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]