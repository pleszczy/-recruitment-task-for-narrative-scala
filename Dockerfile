
FROM hseeberger/scala-sbt:graalvm-ce-21.3.0-java17_1.6.2_3.1.1

RUN mkdir /app
RUN ls
COPY  ./narrative-analytics.jar /app

CMD ["java","-jar","/app/narrative-analytics.jar"]