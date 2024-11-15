# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Make Gradle wrapper executable
RUN chmod +x ./gradlew

# Expose the port the app runs on
EXPOSE 8081

# Run the application
CMD ["./gradlew", "bootRun"]