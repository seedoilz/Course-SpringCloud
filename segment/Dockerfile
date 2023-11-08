FROM openjdk:17

WORKDIR /app

COPY target/client-0.0.1-SNAPSHOT.jar /app/client.jar

#COPY server/target/server-0.0.1-SNAPSHOT.jar /app/server.jar

EXPOSE 8070 8071

#CMD ["java", "-jar", "server.jar"]

CMD ["java", "-jar", "client.jar"]