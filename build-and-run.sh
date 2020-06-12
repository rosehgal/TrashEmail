#! /bin/bash

environment=${1:-dev}
export SPRING_PROFILES_ACTIVE=$environment

make $environment
docker-compose up


