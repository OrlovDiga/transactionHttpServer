#FROM fabric8/java-alpine-openjdk11-jre
#EXPOSE 8001
#RUN mkdir -p /app/
#ADD target/transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/
#CMD java -jar /app/transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar
FROM fabric8/java-alpine-openjdk11-jre
WORKDIR /
ADD target/transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar"]
