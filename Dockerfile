FROM gradle:jdk21-alpine AS build

WORKDIR /usr/app
COPY --chown=gradle:gradle . .
RUN ./gradlew bootJar --no-daemon

FROM amazoncorretto:21-alpine
LABEL authors="brendenehlers"

WORKDIR /usr/app
COPY --from=build /usr/app/build/libs/shortener-service*.jar url-shortener.jar
ENTRYPOINT ["java", "-jar", "url-shortener.jar"]