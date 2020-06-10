FROM balenalib/raspberry-pi-openjdk:8-stretch-20200604
ADD target/LedControllerApi.jar LedControllerApi.jar
ADD src/main/resources/docker_application.properties docker_application.properties
ADD src/main/resources/docker_application.properties application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "LedControllerApi.jar" , "--spring.config.location=docker_application.properties"]
#ENTRYPOINT ["java", "-jar", "LedControllerApi.jar" , "--spring.config.location=application.properties"]
