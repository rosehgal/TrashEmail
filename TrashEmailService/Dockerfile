FROM openjdk:8-jre-alpine

ARG SPRING_PROFILES_ACTIVE

RUN mkdir -p /opt/TrashEmailService/

COPY ./target/*.jar /opt/TrashEmailService/trashemailservice.jar  
WORKDIR /opt/TrashEmailService/


EXPOSE 9090  
ENTRYPOINT ["java","-jar","trashemailservice.jar"] 
