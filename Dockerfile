FROM openjdk:17 as build
WORKDIR /app
COPY . ./
RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/target/peaksoftlms-b6-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "peaksoftlms-b6-0.0.1-SNAPSHOT.jar"]
EXPOSE 5000