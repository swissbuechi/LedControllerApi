package com.bbh.LedControllerApi;

import com.bbh.LedControllerApi.services.imageService.ImageInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ImageInputTests {

	private static final Logger LOGGER = LogManager.getLogger(ImageInputTests.class);

	@Autowired
	private ImageInput imageInput;

	@Test
	void testLocalImageInputGetImage() {
		File file = null;
		try {
			file = ResourceUtils.getFile("classpath:test1.jpg");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		assertThat(file).isNotNull();
		BufferedImage image = imageInput.getImage(file.getAbsolutePath());
		assertThat(image).isNotNull();
		assertThat(image.getHeight()).isEqualTo(1024);
		assertThat(image.getWidth()).isEqualTo(768);
	}

	@Test
	void testOnlineImageInputGetImage() {
		BufferedImage image = imageInput.getImage("https://testimages.org/img/testimages_screenshot.jpg");
		assertThat(image).isNotNull();
		assertThat(image.getHeight()).isEqualTo(338);
		assertThat(image.getWidth()).isEqualTo(600);
	}

	@Test
	void testOnlineImageInputInvalidFileTypeGetImage() {
		BufferedImage image = imageInput.getImage("https://upload.wikimedia.org/wikipedia/commons/8/80/Wikipedia-logo-v2.svg");
		assertThat(image).isNull();
	}
}
