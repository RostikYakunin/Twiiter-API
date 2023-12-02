FROM gradle:8.1.1-jdk17-jammy

WORKDIR /app
COPY . .
RUN gradle -x clean build

COPY build/libs/twitter_mini.war app.war
CMD ["java", "-jar", "app.war"]