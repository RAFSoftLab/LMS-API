# Use a base image with Java 17
FROM eclipse-temurin:17-jdk-alpine


# Add necessary packages
RUN apk add --no-cache git bash sudo

# Add www-data user (group already exists)
RUN adduser -S www-data -G www-data || true

# Copy the JAR package into the image
VOLUME /tmp
COPY build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8091

# Run the App
ENTRYPOINT ["java","-jar","/app.jar"]