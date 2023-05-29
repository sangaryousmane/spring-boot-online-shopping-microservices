FROM openjdk:17

ARG JAR_URL=target/*.jar

COPY ${JAR_URL} customer-service.jar

ENTRYPOINT['java', '-jar', '/customer-service.jar']

EXPOSE 9000

