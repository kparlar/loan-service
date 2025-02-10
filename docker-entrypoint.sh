#!/bin/bash
set -e

if [ -z "${DB_URL}" ]; then
  echo "No DB_URL"
else
  echo "DB_URL=${DB_URL}"
fi

if [ -z "${DB_PORT}" ]; then
  echo "No DB_PORT"
else
  echo "DB_PORT=${DB_PORT}"
fi

if [ -z "${DB_NAME}" ]; then
  echo "No DB_NAME"
else
  echo "DB_NAME=${DB_NAME}"
fi

if [ -z "${DB_USERNAME}" ]; then
  echo "No DB_USERNAME"
else
  echo "DB_USERNAME=${DB_USERNAME}"
fi

if [ -z "${DB_PASSWORD}" ]; then
  echo "No DB_PASSWORD"
else
  echo "DB_PASSWORD=${DB_PASSWORD}"
fi

java -jar -Dspring.profiles.active=local loan-service.jar

exec "$@"

