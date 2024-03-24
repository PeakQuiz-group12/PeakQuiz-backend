FROM openjdk:21-jdk

WORKDIR /app

COPY target/peakQuiz-backend-0.0.1-SNAPSHOT.jar /app/peakQuiz-backend.jar

# To run:
    # - docker build -t peakquiz-backend .
    # -  docker run -d -p 8080:8080 peakquiz-backend
CMD ["java", "-jar", "peakQuiz-backend.jar"]