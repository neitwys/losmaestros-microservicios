FROM maven:3.9-eclipse-temurin-21 AS build
ARG SERVICIO
WORKDIR /build
COPY ${SERVICIO}/pom.xml ./pom.xml
COPY ${SERVICIO}/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/*.jar /app/app.jar
ENTRYPOINT ["java","-Xmx256m","-jar","app.jar"]
