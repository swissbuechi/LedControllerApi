FROM openjdk:8
ADD target/LedControllerApi.jar LedControllerApi.jar
ADD src/main/resources/docker_application.properties docker_application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "LedControllerApi" , "--spring.config.location=docker_application.properties"]