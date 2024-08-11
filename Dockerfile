FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY "build/libs/awsome-pizza-0.0.1-SNAPSHOT.jar" "awsome-pizza-0.0.1-SNAPSHOT.jar"
ENTRYPOINT ["java","-jar","/awsome-pizza-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080