package com.bbh.LedControllerApi.services.tttService;

import com.bbh.LedControllerApi.services.imageService.ImageToRGB;
import com.bbh.LedControllerApi.services.imageService.MatrixBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class Players {

    private static final Logger LOGGER = LogManager.getLogger(Players.class);
    @Autowired
    private ImageToRGB imageToRGB;
    @Autowired
    private MatrixBuilder matrixBuilder;

    public String[][] drawX(String[][] image, int positionRow, int positionColumn) {
        Color xColorRgb = new Color(0, 255, 0);
        String xColor = imageToRGB.buildGrbString(xColorRgb);
        image = matrixBuilder.fillArray(image, (positionRow * 6), (positionColumn * 6), xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6), (positionColumn * 6) + 3, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 1, (positionColumn * 6) + 1, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 1, (positionColumn * 6) + 2, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 2, (positionColumn * 6) + 1, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 2, (positionColumn * 6) + 2, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 3, (positionColumn * 6), xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 3, (positionColumn * 6) + 3, xColor);
        return image;
    }

    public String[][] drawO(String[][] image, int positionRow, int positionColumn) {
        Color xColorRgb = new Color(255, 0, 0);
        String xColor = imageToRGB.buildGrbString(xColorRgb);
        image = matrixBuilder.fillArray(image, (positionRow * 6), (positionColumn * 6) + 1, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6), (positionColumn * 6) + 2, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 1, (positionColumn * 6), xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 2, (positionColumn * 6), xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 1, (positionColumn * 6) + 3, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 2, (positionColumn * 6) + 3, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 3, (positionColumn * 6) + 1, xColor);
        image = matrixBuilder.fillArray(image, (positionRow * 6) + 3, (positionColumn * 6) + 2, xColor);
        return image;
    }

    public int setPosition(Integer[] position) {
        return 1;
    }

}
