package com.bbh.LedControllerApi.services.imageService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MatrixBuilder {
    private static final Logger LOGGER = LogManager.getLogger(MatrixBuilder.class);

    public String[][] fillArray(String[][] image, int row, int column, String color) {
        if ("000000" == image[row][column])
            image[row][column] = color;
        return image;
    }

    public String[][] initializeArray(String[][] image) {
        for (int row = 0; row < image.length; row++) {
            Arrays.fill(image[row], "000000");
        }
        return image;
    }

    public String[][] initializeArray() {
        String[][] image = new String[16][16];
        for (int row = 0; row < image.length; row++) {
            for (int column = 0; column < image[row].length; column++) {
                image[row][column] = "000000";
            }
        }
        return image;
    }
}
