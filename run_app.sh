#!/bin/bash

echo "Cleaning up port 8080..."
PID=$(lsof -t -i:8080)
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "Terminated process $PID"
fi

echo "Verifying Cassandra container..."
if [ ! "$(docker ps -q -f name=flex-cassandra)" ]; then
    echo "Starting Cassandra..."
    docker start flex-cassandra || docker run --name flex-cassandra -p 9042:9042 -d cassandra:latest
fi

echo "Building and Running FlexStream..."
mvn clean spring-boot:run
