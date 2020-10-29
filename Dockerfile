FROM ubuntu:xenial
RUN apt update
RUN apt install openjdk-8-jdk -y
RUN apt install maven -y
VOLUME /src
WORKDIR /src
CMD "bash"