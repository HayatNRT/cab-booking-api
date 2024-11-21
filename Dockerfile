FROM ubuntu:24.04
LABEL authors="HAYAT"
RUN mkdir -p /u01/app
RUN apt update -y && apt upgrade -y
RUN apt install -y openjdk-17-jdk
WORKDIR /u01/app
COPY target/uberapi-1.0.jar .
EXPOSE 8081
ENTRYPOINT ["java","-jar" ,"uberapi-1.0.jar"]