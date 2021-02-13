FROM fabric8/java-alpine-openjdk11-jre
EXPOSE 8001
RUN mkdir -p /app/
ADD target/transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar"]