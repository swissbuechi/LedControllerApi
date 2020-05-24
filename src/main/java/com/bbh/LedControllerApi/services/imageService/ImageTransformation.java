package com.bbh.LedControllerApi.services.imageService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.Arrays;

@Component
public class ImageTransformation {

    private static final Logger LOGGER = LogManager.getLogger(ImageTransformation.class);

    /**
     * Quelle: https://stackoverflow.com/questions/991349/resize-image-while-keeping-aspect-ratio-in-java/6585887
     * <p>
     * Skaliert das übergebene Bild auf eine neue Grösse. Bild kann vergrössert oder verkleinert werden.
     * Das Seitenverhältnis bleibt bestehen. Massgebend für das vergrössern ist die Seite, welche beim Vergrössern
     * näher an der neuen Grösse liegt. Also es wird die kleinere Seite bevorzugt.
     * <p>
     * Beispiel: Bild mit 1200x900 soll auf 140x150 verkleinert werden. Das Bild wird danach auf 140x105 skaliert.
     * Beispiel2: Bild mit 1200x900 soll 1400x1300 vergrössert werden. Das Bild wird danach auf 1400x1050 skaliert
     *
     * @param image  Das eingelesene Bild
     * @param newDim Neue maximale Länge und Höhe.
     * @return Das neu skalierte Bild
     */
    public BufferedImage getScaledImage(BufferedImage image, Dimension newDim) {
        LOGGER.info("Image scaled to " + newDim.toString());
        // Berechnet die Skalierung der neuen von der Ursprungsgrösse. Double weil jenachdem Nachkomma relevant.
        double scaleX = newDim.getWidth() / image.getWidth();
        double scaleY = newDim.getHeight() / image.getHeight();
        // Nimmt den kleineren Wert als Skalierungsfaktor
        double scale = Math.min(scaleX, scaleY);

        // Berechnung der Skalierung der neue Höhe und Breite
        int w = (int) (image.getWidth() * scale);
        int h = (int) (image.getHeight() * scale);

        // Erzeugt eine skalierte Version des ursprünglichen Bildes. SCALE_SMOOTH = Priorität liegt eher bei Smooth
        // als bei der Schnelligkeit.
        Image tmp = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        // Erzeugt ein neues skaliertes BufferedImage
        BufferedImage resized = new BufferedImage(w, h, image.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    /**
     * Quadratisches einpassen.
     *
     * @param image Das eingelesene Bild
     * @param s     Eine Seite, auf die quadratisch eingepasst werden muss
     * @return Skaliertes Bild
     */
    public BufferedImage squareFit(BufferedImage image, int s) {
        LOGGER.info("Image scaled to " + s + "x" + s);
        return getScaledImage(image, new Dimension(s, s));
    }

    /**
     * Transponiert ein eingelesenes Bild.
     * <p>
     * Dabei wird quasi eine Diagonale von der linken oberen Ecke in die rechte untere Ecke gezogen und an dieser
     * gedachten Linie das Bild gespiegelt.
     *
     * @param bufferedImage Das eingelesene Bild
     * @return Ein transponiertes Bild
     */
    public BufferedImage transpose(BufferedImage bufferedImage) {
        // Buffered Image in Color-Array laden. Höhe und Breite vertauschen, da transponieren!
        Color[][] colors = new Color[bufferedImage.getWidth()][bufferedImage.getHeight()];

        // Geht pixel für Pixel durch und speichert die Farbe im Colors-Array. Anhand des Color-Arrays wird nachher
        // das Bild transponiert.
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                colors[x][y] = new Color(bufferedImage.getRGB(x, y));
            }
        }

        // Neues BufferedImage bauen für Rückgabe. RGB verwenden.
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getHeight()
                , bufferedImage.getWidth(), BufferedImage.TYPE_INT_RGB);

        // In diesen beiden Schleifen wird das Bild tranponiert und Schritt für Schritt als newBufferedImage aufgebaut.
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[0].length; j++) {
                // X und Y Ache vertauschen! X geht auf Y-Achse und Y auf X-Achse
                newBufferedImage.setRGB(j, i, colors[i][j].getRGB());
            }
        }
        LOGGER.info("Image transposed");
        return newBufferedImage;
    }

    /**
     * Bei einem grossen Bild soll ein Teilausschnitt davon abgefragt und später verwendet werden. Diese Methode hilft
     * dabei ein Bildausschnitt zu selektieren.
     *
     * @param image Das eingelesene Bild
     * @param scale startPosX Die X-Koordinate bei dem der Bildausschnitt gestartet wird
     *              startPosY Die Y-Koordinate bei dem der Bildausschnitt gestartet wird
     *              width     Die Länge des neuen Bildausschnitts
     *              height    Die Höhe des neuen Bildausschnitts
     * @return Der Teilausschnitt von Bild
     */
    public BufferedImage getSubimage(BufferedImage image, Integer[] scale) {
        try {
            LOGGER.info("Image cropped to x-axis, y-axis, width, height: " + Arrays.toString(scale));
            int startPosX = scale[0];
            int startPosY = scale[1];
            int width = scale[2];
            int height = scale[3];
            return image.getSubimage(startPosX, startPosY, width, height);
        } catch (RasterFormatException Rfe) {
            LOGGER.error("Cropping not possible, selected part is outside of the image");
        }
        return null;
    }


    /**
     * Quelle: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java/37758533
     * <p>
     * Dreht das Bild um den angegebenen Wert in Grad
     * Werte von 0-360.
     * <p>
     * Beispiel: Bild soll um 90 grad gedreht werden.
     *
     * @param image Das eingelesene Bild
     * @param angle Den Wert für die Drehung in Grad 0-360.
     * @return Das neu skalierte Bild
     */
    public BufferedImage rotate(BufferedImage image, double angle) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, image.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w / 2, h / 2);
        graphic.drawImage(image, null, 0, 0);
        graphic.dispose();
        LOGGER.info("Image rotated by " + angle + " degree");
        return rotated;
    }
}
