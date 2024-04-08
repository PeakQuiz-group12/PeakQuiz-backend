FROM openjdk:21-jdk

WORKDIR /app

COPY target/peakQuiz-backend-0.0.1-SNAPSHOT.jar /app/peakQuiz-backend.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","peakQuiz-backend.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]

CMD ["java", "-jar", "peakQuiz-backend.jar"]