FROM openjdk:17

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y findutils
RUN ./gradlew

COPY build/libs/twitter_mini.jar app.jar

ENV MONGODB_URI=mongodb://localhost:27017/twitter

CMD ["java", "-jar", "app.jar"]