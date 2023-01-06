FROM java:11
LABEL maintainer="woooo96_project"
VOLUME /home/ubuntu
EXPOSE 8080
ARG JAR_FILE=build/libs/alba_pocket-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} alba_pocket.jar
ENTRYPOINT ["nohup","java","-jar","/alba_pocket.jar"]

