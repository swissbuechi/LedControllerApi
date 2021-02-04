package com.bbh.LedControllerApi.controllers;

import com.bbh.LedControllerApi.forms.ImageForm;
import com.bbh.LedControllerApi.gateways.serial.ComPort;
import com.bbh.LedControllerApi.services.imageService.*;
import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

@RestController
public class ImageController {
    private static final Logger LOGGER = LogManager.getLogger(ImageController.class);
    private static SerialPort port;
    private static OutputStream stream;
    @Autowired
    private ComPort comPort;
    @Autowired
    private ImageInput imageInput;
    @Autowired
    private ImageToRGB imageToRGB;
    @Autowired
    private ImageTransformation imageTransformation;
    @Autowired
    private SaveImage saveImage;
    @Autowired
    private MatrixBuilder matrixBuilder;
    @Value("${comport}")
    private String CustomComPort;

    @PostMapping("/image")
    public ResponseEntity<String> imageToLed(@RequestBody ImageForm imageForm) {
        imageForm.matrix = new Integer[]{16, 16};
        try {
            if (imageForm.reset) {
                port = comPort.open(CustomComPort);
                stream = port.getOutputStream();
                comPort.write(stream, imageToRGB.GrbArrayToString(matrixBuilder.initializeArray()).getBytes());
                stream.close();
                comPort.close(port);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            // Es wird überprüft ob kein Bild zur darstellung angegeben wurde
            if (imageForm.inputFile == null) {
                LOGGER.error("No input image file provided: Displaying sample image");
                //Schreibt das Sample Image auf den seriellen Port
                imageForm.inputFile = "img/img.png";
            }
            // Das Bild wird eingelesen
            BufferedImage image = imageInput.getImage(imageForm.inputFile);
            if (image == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            int panelWidth = imageForm.matrix[0];
            int panelHeight = imageForm.matrix[1];

            // Das Bild wird zugeschnitten
            if (imageForm.cropImage != null) image = imageTransformation.getSubimage(image, imageForm.cropImage);

            // Das Bild wird skalliert
            if (imageForm.scale != null) {
                Dimension scaleDimension = new Dimension(imageForm.scale[0], imageForm.scale[1]);
                image = imageTransformation.getScaledImage(image, scaleDimension);
            }
            // Das Bild wird quadratisch eingepasst
            //if (imageForm.square != null) {
            //    image = imageTransformation.squareFit(image, imageForm.square);
            //}
            if (imageForm.square != null) image = imageTransformation.squareFit(image, imageForm.square);

            // Das Bild wird transponiert
            if (imageForm.transpose) image = imageTransformation.transpose(image);

            // Das Bild wird gedreht
            if (imageForm.rotate != null) image = imageTransformation.rotate(image, imageForm.rotate);

            // Das Bild wird gespeichert
            if (imageForm.saveFile != null) saveImage.save(image, imageForm.saveFile);

            // Wenn das Auto-Scaling deaktiviert werden soll
            if (imageForm.disableAutoscale) {
                LOGGER.info("Autoscaling disabled");
            }
            // Autoscaling aktiv
            if (image.getWidth() > panelWidth || image.getHeight() > panelHeight && !imageForm.disableAutoscale) {
                Dimension matrixDimension = new Dimension(imageForm.scale[0], imageForm.scale[1]);
                image = imageTransformation.getScaledImage(image, matrixDimension);
                LOGGER.error("Image is to big to be displayed on the " + panelWidth + "x" + panelHeight + " matrix");
                LOGGER.error("Autoscaling image down to: " + panelWidth + "x" + panelHeight);
            }
            // Die Grösse der Matrix
            if (panelHeight != 16 && panelWidth != 16) {
                LOGGER.info("LED matrix size set to " + Arrays.toString(imageForm.matrix));
            }

            // Die Helligkeit der LEDs
            if (imageForm.brightness == null) {
                imageForm.brightness = 0.1d;
            }

            LOGGER.info("LED brightness level " + imageForm.brightness);
            String grbString = imageToRGB.imageToString(image, imageForm.brightness, panelWidth, panelHeight);
            // Das Bild wird auf den seriellen Port geschrieben
            port = comPort.open(CustomComPort);
            stream = port.getOutputStream();
            System.out.println(grbString);
            comPort.write(stream, grbString.getBytes());
            stream.close();
            comPort.close(port);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException WriteException) {
            LOGGER.error("Write Error " + WriteException.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}