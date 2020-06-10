#! /bin/bash

version=1.1.1
SPRING_PROFILES_ACTIVE=${1:-qa}
export SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

# Build the jars.
cd ./TrashEmailService && mvn clean install -Dmaven.test.skip=true && cd ../
cd ./EmailsToTelegramService &&  mvn clean install -Dmaven.test.skip=true && cd ../

# Build the Dockers
docker-compose build
docker-compose up


