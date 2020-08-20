package com.bbh.LedControllerApi.services.TttService;

import com.bbh.LedControllerApi.services.imageService.ImageToRGB;
import com.bbh.LedControllerApi.services.imageService.MatrixBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;

@Component
public class Board {

//    private static final Logger LOGGER = LogManager.getLogger(Board.class);
    @Autowired
    private ImageToRGB imageToRGB;

    @Autowired
    private MatrixBuilder matrixBuilder;

    @Autowired
    private Players players;

    // Achtung, hier ist der Input ein CharacterArray, zurückgegeben wird ein StringArray!

    public String[][] turn(Character[][] positions) {
        String[][] image = getGrid();
        for (int row = 0; row < positions.length; row++) {
            for (int column = 0; column < positions[row].length; column++) {
                Boolean cell = isX(positions[row][column]);
                if (cell != null && cell) {
                    image = players.drawX(image, row, column);
                } else if (cell != null) { // Abfrage: cell == false bzw. !cell ist nicht nötig, da nur True oder False möglich und bereits True oben abgefragt
                    image = players.drawO(image, row, column);
                }
            }
        }
        return image;
    }

    public String[][] getGrid() {
        String[][] image = new String[16][16];
        image = matrixBuilder.initializeArray(image);
        Color gridColorRgb = new Color(000, 000, 255);
        String gridColor = imageToRGB.buildGrbString(gridColorRgb);
        Arrays.fill(image[4], gridColor);
        Arrays.fill(image[5], gridColor);
        Arrays.fill(image[10], gridColor);
        Arrays.fill(image[11], gridColor);
        for (int row = 0; row < image.length; row++) {
            for (int column = 0; column < image[row].length; column++) {
                image = matrixBuilder.fillArray(image, row, 4, gridColor);
                image = matrixBuilder.fillArray(image, row, 5, gridColor);
                image = matrixBuilder.fillArray(image, row, 10, gridColor);
                image = matrixBuilder.fillArray(image, row, 11, gridColor);
            }
        }
        return image;
    }

    private Boolean isX(Character field) {
        return field != null ? StringUtils.equalsIgnoreCase(field.toString(), "x") : null;
    }
}
