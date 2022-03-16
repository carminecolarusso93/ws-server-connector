FROM maven:3.8.4-openjdk-11

COPY src/ /tmp/application/src/
COPY pom.xml /tmp/application/
#COPY entrypoint.sh /usr/local

WORKDIR /tmp/application/

RUN mvn install

#Websocket Server port
EXPOSE 8025/tcp

WORKDIR /tmp/application/target

#Launch tests
ENTRYPOINT java -jar WSSinkConnector-0.0.1-jar-with-dependencies.jar $(hostname -I)
