FROM openjdk:8-jre-alpine

ARG SPRING_PROFILES_ACTIVE

RUN mkdir -p /opt/EmailsToTelegramService

COPY ./target/*.jar /opt/EmailsToTelegramService/emailstotelegramservice.jar  
WORKDIR /opt/EmailsToTelegramService/

RUN addgroup -S emailstotelegram  && adduser -S emailstotelegram -G emailstotelegram
USER emailstotelegram

ENTRYPOINT ["java","-jar","emailstotelegramservice.jar"] 
