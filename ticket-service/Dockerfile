#
# Build stage
#
FROM maven:3.9.9-amazoncorretto-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -DskipTests -f /home/app/pom.xml clean package 

#
# Package stage
#
FROM amazoncorretto:21.0.4-alpine3.20
COPY --from=build /home/app/target/ticket-service-0.0.1-SNAPSHOT.jar /usr/local/lib/ticket-service-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/ticket-service-0.0.1-SNAPSHOT.jar"]