FROM gradle:8.1.1-jdk17-jammy AS builder

WORKDIR /app
COPY build.gradle /app/
COPY settings.gradle /app/
RUN gradle build --no-daemon > /dev/null 2>&1 || true

COPY ./ /app/
RUN gradle build

FROM builder

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
