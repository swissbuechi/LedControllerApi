package com.bbh.LedControllerApi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class LedControllerApiApplication implements ApplicationRunner {

	private static final Logger LOGGER = LogManager.getLogger(LedControllerApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LedControllerApiApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		if (args.getSourceArgs().length != 0) LOGGER.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
	}
}