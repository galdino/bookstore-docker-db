# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

# Copy jar file
COPY bookstore-0.0.1-SNAPSHOT.jar /bookstore-0.0.1-SNAPSHOT.jar

# run the jar
CMD ["/usr/bin/java", "-jar", "/bookstore-0.0.1-SNAPSHOT.jar"]