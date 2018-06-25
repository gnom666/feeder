FROM openjdk:8
ADD target/elasticsearch-feeder-0.0.1.jar feeder-1.0.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "feeder-1.0.jar"]