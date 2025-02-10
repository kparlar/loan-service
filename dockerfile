FROM openjdk:21-jdk-oraclelinux8

ARG ARG_DB_URL=db
ARG ARG_DB_PORT=5432
ARG ARG_DB_NAME=postgres
ARG ARG_DB_USERNAME=postgres
ARG ARG_DB_PASSWORD=changeme

ENV DB_URL=$ARG_DB_URL
ENV DB_PORT=$ARG_DB_PORT
ENV DB_NAME=$ARG_DB_NAME
ENV DB_USERNAME=$ARG_DB_USERNAME
ENV DB_PASSWORD=$ARG_DB_PASSWORD

USER root

# Create app directory
WORKDIR /app

COPY target/loan-service.jar ./
COPY docker-entrypoint.sh docker-entrypoint.sh

EXPOSE 8081
RUN ["chmod", "+x", "docker-entrypoint.sh"]
ENTRYPOINT ["./docker-entrypoint.sh"]
