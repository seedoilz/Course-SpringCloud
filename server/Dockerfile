FROM openjdk:17

WORKDIR /app

#COPY segment/target/client-0.0.1-SNAPSHOT.jar /app/client.jar

COPY target/server-0.0.1-SNAPSHOT.jar /app/server.jar

EXPOSE 8071

CMD ["java", "-jar", "server.jar"]

#CMD ["java", "-jar", "client.jar"]