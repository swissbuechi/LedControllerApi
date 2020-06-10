FROM balenalib/raspberry-pi-openjdk
ADD target/LedControllerApi.jar LedControllerApi.jar
ADD src/main/resources/docker_application.properties docker_application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "LedControllerApi.jar" , "--spring.config.location=docker_application.properties"]