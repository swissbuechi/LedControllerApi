package com.bbh.LedControllerApi.gateways.serial;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class ComPort {

    public static final int BITRATE = 921600;
    private static final Logger LOGGER = LogManager.getLogger(ComPort.class);
    // Serial no parity mode
    private static final int NO_PARITY = 0;

    // Serial timeout not blocking
    private static final int TIMEOUT_NONBLOCKING = 0;

    /**
     * Öffnet einen Outputstream über einen bestimmten Seriellen Port
     * <p>
     *
     * @param CustomComPort Der Serielle Port
     * @return Den Outputstream über den Seriellen Port
     */
    public SerialPort open(String CustomComPort) {
        if (CustomComPort.equals("auto")) {
            return open();
        }

        SerialPort customPort = SerialPort.getCommPort(CustomComPort);
        // Erstellt ein Array mit allen verfügbaren seriellen Ports
        SerialPort[] allAvailableComPorts = SerialPort.getCommPorts();
        boolean portAvailable = false;
        for (SerialPort allAvailableComPort : allAvailableComPorts) {
            // Überprüft ob der Serielle Port verfügbar ist
            if (allAvailableComPort.getSystemPortName().equals(customPort.getSystemPortName())) {
                portAvailable = true;
                break;
            }
        }
        try {
            if (portAvailable) {
                //
                customPort.setComPortParameters(BITRATE, 8, 1, NO_PARITY);
                // Setzt die Timeouts vom Seriellen Port
                customPort.setComPortTimeouts(TIMEOUT_NONBLOCKING, 0, 0);
                // Öffnet den Seriellen Port
                customPort.openPort();
                LOGGER.info("Opened the Port " + customPort.getSystemPortName());
                // Erstelllt einen Outputstream
                return customPort;
            } else {
                LOGGER.error("The custom serial port " + CustomComPort + " is not available");
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to open " + customPort.getSystemPortName());
            return null;
        }
    }

    /**
     * Öffnet einen Outputstream über den ersten verfügbaren Seriellen Port am System
     * <p>
     *
     * @return Den Outputstream über den Seriellen Port
     */
    public SerialPort open() {
        SerialPort firstAvailableComPort;
        // Erstellt ein Array mit allen verfügbaren seriellen Ports
        SerialPort[] allAvailableComPorts = SerialPort.getCommPorts();
        if (allAvailableComPorts.length == 0) {
            LOGGER.error("No serial port found");
            return null;
            // Das Array wird ausgegeben
        } else {
            for (SerialPort eachComPort : allAvailableComPorts) {
                LOGGER.info("List of all available serial ports: " + eachComPort.getSystemPortName());
            }
            // Der erste verfügbare Port wird ausgewählt
            firstAvailableComPort = allAvailableComPorts[0];
            // Setzt die Parameter vom Seriellen Port
            try {
                firstAvailableComPort.setComPortParameters(921600, 8, 1, NO_PARITY);
                // Setzt die Timeouts vom Seriellen Port
                firstAvailableComPort.setComPortTimeouts(TIMEOUT_NONBLOCKING, 0, 0);
                // Öffnet den Seriellen Port
                firstAvailableComPort.openPort();
                LOGGER.info("Opened the first available serial port: " + firstAvailableComPort.getSystemPortName());
                // Erstelllt einen Outputstream
                return firstAvailableComPort;
            } catch (Exception e) {
                LOGGER.error("Failed to open " + firstAvailableComPort.getSystemPortName());
            }
        }
        return null;
    }

    /**
     * Schreibt ein ByteArray auf einen Outputstream
     * <p>
     *
     * @param outputStream Den Outputstream, auf welchen geschriebenw erden seoll
     * @param bytestream   das ByteArray, welches auf den Outputstream geschriben werden soll
     */
    public void write(OutputStream outputStream, byte[] bytestream) {
        try {
            outputStream.write(bytestream);
            LOGGER.info("Writing to serial port");
        } catch (IOException WriteException) {
            LOGGER.error("Could not write to serial Port: " + WriteException.getMessage());
        }
    }

    /**
     * Schiesst den Outputstream.
     * <p>
     *
     * @param ComPort Der Outputstream, welcher geschlossen werden soll
     */
    public void close(SerialPort ComPort) {
        ComPort.closePort();
        LOGGER.info("Closed Port: " + ComPort.getSystemPortName());
    }
}
