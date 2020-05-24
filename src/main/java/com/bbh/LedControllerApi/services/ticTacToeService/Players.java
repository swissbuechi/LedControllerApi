package com.bbh.LedControllerApi.services.ticTacToeService;

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
        Color xColorRgb = new Color(000, 255, 000);
        String xColor = imageToRGB.buildGrbString(xColorRgb);
        image = matrixBuilder.fillArray(image, positionRow + 0, positionColumn + 0, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 0, positionColumn + 3, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 1, positionColumn + 1, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 1, positionColumn + 2, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 2, positionColumn + 1, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 2, positionColumn + 2, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 3, positionColumn + 0, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 3, positionColumn + 3, xColor);
        return image;
    }

    public String[][] drawO(String[][] image, int positionRow, int positionColumn) {
        Color xColorRgb = new Color(255, 000, 000);
        String xColor = imageToRGB.buildGrbString(xColorRgb);
        image = matrixBuilder.fillArray(image, positionRow + 0, positionColumn + 1, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 0, positionColumn + 2, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 1, positionColumn + 0, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 2, positionColumn + 0, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 1, positionColumn + 3, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 2, positionColumn + 3, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 3, positionColumn + 1, xColor);
        image = matrixBuilder.fillArray(image, positionRow + 3, positionColumn + 2, xColor);
        return image;
    }

    public int setPosition(Integer[] position) {
        return 1;
    }

}
