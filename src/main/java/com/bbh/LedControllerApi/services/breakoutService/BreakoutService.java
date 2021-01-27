package com.bbh.LedControllerApi.services.breakoutService;

import com.bbh.LedControllerApi.forms.BreakoutEvent;
import com.bbh.LedControllerApi.gateways.serial.ComPort;
import com.bbh.LedControllerApi.services.imageService.ImageToRGB;
import com.bbh.LedControllerApi.services.imageService.MatrixBuilder;
import com.bbh.LedControllerApi.services.tttService.Board;
import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BreakoutService {

    private static final Logger LOGGER = LogManager.getLogger(BreakoutService.class);

    // Kein Autowired hier, wird über SetterMethoden ganz unten gemacht. https://www.baeldung.com/spring-autowire
    // Alternative wäre über Constructor, aber mit 4 Parametern ists etwas lang.

    private MatrixBuilder matrixBuilder;
    private Board playground;
    private ComPort comPort;
    private ImageToRGB imageToRGB;

    @Value("${comport}")
    private String CustomComPort;

    public void handleTurn(BreakoutEvent breakoutEvent) {
        if (breakoutEvent.isReset()) {
            sendToPort(matrixBuilder.initializeArray());
            return;
        }
        if (breakoutEvent.getRgb() != null) {

            //Handle Snake Line handle rgb grb

            sendToPort(breakoutEvent.getRgb());
        }
    }

    private void sendToPort(String[][] stringArray) {
        SerialPort port = comPort.open(CustomComPort);
        if (port != null) {
            comPort.write(port.getOutputStream(), imageToRGB.RgbArrayToString(stringArray, 0.01).getBytes());
            comPort.close(port);
        }
    }

    /* DipendencyInjection über setter Methoden */

    @Autowired
    public void setMatrixBuilder(MatrixBuilder matrixBuilder) {
        this.matrixBuilder = matrixBuilder;
    }

    @Autowired
    public void setBoard(Board playground) {
        this.playground = playground;
    }

    @Autowired
    public void setComPort(ComPort comPort) {
        this.comPort = comPort;
    }

    @Autowired
    public void setImageToRGB(ImageToRGB imageToRGB) {
        this.imageToRGB = imageToRGB;
    }

}
