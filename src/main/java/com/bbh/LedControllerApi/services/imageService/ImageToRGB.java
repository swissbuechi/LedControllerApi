package com.bbh.LedControllerApi.services.imageService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Component
public class ImageToRGB {
    private final ImageTransformation imageTransformation = new ImageTransformation();

    /**
     * <p>Liest das Bild Zeile für Zeile. Das Lesen passiert in einer "Snakeline", was bedeutet, dass man die erste Zeile
     * von links nach rechts liest und die darauffolgende von rechts nach links. Dies geschieht immer abwechslungsweise.
     * </p>
     *
     * <p>Jeder Pixel wird dabei in die einzelnen Farbbestandteile (RGB) aufgefechert. Diese Methode baut einen String
     * der für jede Zeile mit einem "<" eingeleitet wird. Darauf folgt die Zeilennummer als Hexadezimalwert (klein!).
     * Nach der Zeilennummer werden sämtliche Pixel pro Zeile aneinander im GRB-Hexformat (GRB, nicht RGB!) aufgelistet.
     * Zum Schluss folgt ein ">" was der Abschluss der Zeile bedeutet.</p>
     *
     * <p>Die Rückgabe ist ein einzelner langer String. Getrennt werden die einzelnen Zeilen-Teile mit einem Komma
     * (ArrayList.toString()).</p>
     *
     * <p>Der Fertige String sieht so aus
     * <br/><code><01FFFFF.....000>, <02000.....FFFFF>, ....</code></p>
     *
     * @param image       das eingelesene Bild
     * @param brightness  die Helligkeit in Prozent (1 = 100%, 0.1 = 10%, 0.5 = 50%)
     * @param panelWidth  Breite des Panels
     * @param panelHeight Höhe des Panels
     * @return Bild im benötigten Format (GRB String)
     *
     */
    public String imageToString(BufferedImage image, double brightness, int panelWidth, int panelHeight) throws NegativeArraySizeException {
        ArrayList<String> output = new ArrayList<>();
        // Zeilennummern beginnen mit 1, nicht mit 0!
        int lineNumber = 1;
//        // Skaliert das Bild automatisch, falls es zu gross oder zu klein für das Panel ist, falls nichts angegeben ist.
//        image = automaticScaleImageToGivenPanelSize(image, panelWidth, panelHeight);
        if (image.getWidth() < panelWidth || image.getHeight() < panelHeight) {
            image = handleTooSmallImages(image);
        }

        for (int row = 0; row < image.getHeight(); row++) {
            boolean doRead = true;
            String grbLine = "";
            int column = getArrayStart(image.getWidth(), row);
            int halfLedLengthCounter = 0;

            do {
                // Bei der Hälfte der Panellänge muss der String aufteteilt werden. Danach startet wieder ein neuer String.
                // Wird gebraucht damit die Snakeline korrekt erzeugt wird
                if (halfLedLengthCounter != 0 && halfLedLengthCounter % (panelWidth / 2) == 0) {
                    output.add("<" + toFormatedHex(lineNumber++, false) + grbLine + ">");
                    grbLine = "";
                }

                // Baut den GRB bzw. RGB String pro Zeile. Wird jeweils von links her aufgebaut
                grbLine += buildGrbString(new Color(image.getRGB(column, row)), brightness);

                // Snakeline Reset. Lesen der Zeile beenden, wenn Anfang oder Ende erreicht.
                if ((row % 2 == 0 && image.getHeight() - 1 <= column) || (row % 2 != 0 && column <= 0))
                    doRead = false; // Springt aus der Do-While Schleife raus, wenn Kondition erfüllt

                // Variable x (X-Achse) um 1 erhöhen oder reduzieren. --> Je nach Position der Snakeline
                // ungerade Zeilen von rechts lesen, gerade von links
                column = incrementOrDecrement(row, column);
                halfLedLengthCounter++;
            } while (doRead);
            // Baut den GRB String und die benötigten Zusätze zusammen. Zeile startet immer mit "<" gefolgt von der
            // Zeilennummer im Hex-Format (klein), danach kommt die GRB Zeile und der Abschluss ">".
            output.add("<" + toFormatedHex(lineNumber++, false) + grbLine + ">");
        }
        return output.toString();
    }

    public String GrbArrayToString(String[][] image) {
        ArrayList<String> output = new ArrayList<>();
        int lineNumber = 1;
        for (int row = 0; row < image.length; row++) {
            boolean doRead = true;
            String grbLine = "";
            int halfLedLengthCounter = 0;
            int column = getArrayStart(image.length, row);
            do {
                // Bei der Hälfte der Panellänge muss der String aufteteilt werden. Danach startet wieder ein neuer String.
                // Wird gebraucht damit die Snakeline korrekt erzeugt wird
                if (halfLedLengthCounter != 0 && halfLedLengthCounter % 8 == 0) {
                    output.add("<" + toFormatedHex(lineNumber++, false) + grbLine + ">");
                    grbLine = "";
                }

                // Baut den GRB bzw. RGB String pro Zeile. Wird jeweils von links her aufgebaut
                grbLine += image[row][column];

                // Snakeline Reset. Lesen der Zeile beenden, wenn Anfang oder Ende erreicht.
                if ((row % 2 == 0 && image.length - 1 <= column) || (row % 2 != 0 && column <= 0))
                    doRead = false; // Springt aus der Do-While Schleife raus, wenn Kondition erfüllt

                // Variable x (X-Achse) um 1 erhöhen oder reduzieren. --> Je nach Position der Snakeline
                // ungerade Zeilen von rechts lesen, gerade von links
                column = incrementOrDecrement(row, column);
                halfLedLengthCounter++;
            } while (doRead);
            // Baut den GRB String und die benötigten Zusätze zusammen. Zeile startet immer mit "<" gefolgt von der
            // Zeilennummer im Hex-Format (klein), danach kommt die GRB Zeile und der Abschluss ">".
            output.add("<" + toFormatedHex(lineNumber++, false) + grbLine + ">");
        }
        return output.toString();
    }


    /**
     * Gibt abhängig von der gelesenen Zeile die Startkoordinate zum Lesen der jeweiligen Bildzeile zurück.
     *
     * @param imageWidth Die Bildbreite
     * @param lineNumber Die Zeilennummer (Position auf der Row)
     * @return Startposition beim Lesen des Array
     */
    private int getArrayStart(int imageWidth, int lineNumber) {
        if (lineNumber % 2 == 0)
            return 0;
        return imageWidth - 1;
    }

    /**
     * Erhöht oder verringert den Inhalt der Laufvariable abhängig von der aktuellen Zeilennummer. Geradezeilen werden
     * erhöht, ungerade Zeilen um eins verringert.
     *
     * @param lineNumber Die Zeilennummer (Position auf der Row)
     * @param iterator   Die Laufvariable
     * @return Gibt die neue Laufvariable zurück
     */
    private int incrementOrDecrement(int lineNumber, int iterator) {
        if (lineNumber % 2 == 0)
            return iterator + 1;
        return iterator - 1;
    }

    /**
     * Baut einen GRB String zusammen (NICHT RGB). Der Wert wird als Hexstring im Format GGRRBB dargestellt.
     *
     * @param c          Die Farbe
     * @param brightness Die Helligkeit in Prozent (Zwischen 0.0 und 1.0)
     * @return Ein GRB-Farbstring im Format GGRRBB
     */
    public String buildGrbString(Color c, double brightness) {
        return toFormatedHex(adjustBrightness(c.getGreen(), brightness), true)
                + toFormatedHex(adjustBrightness(c.getRed(), brightness), true)
                + toFormatedHex(adjustBrightness(c.getBlue(), brightness), true);
    }

    public String buildGrbString(Color c) {
        double brightness = 0.1d;
        return toFormatedHex(adjustBrightness(c.getGreen(), brightness), true)
                + toFormatedHex(adjustBrightness(c.getRed(), brightness), true)
                + toFormatedHex(adjustBrightness(c.getBlue(), brightness), true);
    }

    /**
     * Formatiert die Farben in einen Hexwert um. Der Hex-Wert wird mit führenden Nullen dargestellt.
     * das bedeutet, falls das Ergebnis nur einstellig wäre, wird auf der linken Seite eine 0 dargestellt.
     * <p>
     * Mit dem zweiten Parameter kann angegeben werden ob der Hexstring gross oder klein geschrieben wird.
     *
     * @param colorValue  Farbewert 0-255
     * @param toUpperCase Gibt an, ob die Buchstaben im Hexstring gross oder klein dargestellt werden.
     * @return Der formatierte Farbcode als Hexstring.
     */
    private String toFormatedHex(int colorValue, boolean toUpperCase) {
        String value = StringUtils.leftPad(Integer.toHexString(colorValue), 2, '0').toLowerCase();
        return toUpperCase ? value.toUpperCase() : value;
    }

    /**
     * <p>Passt die Helligkeit der Farbe an. Helligkeit wird anhand des Farbcodes gesteuert. Die Helligkeit der Farbe
     * wird durch einen höheren Schwarzanteil gesteuert. Je höher der Schwarzteil, desto weniger hell leuchtet die LED
     * danach.</p>
     *
     * <p>In der Methode wird BigDecimal verwendet, weil Rechnen mit double Zahlen ungenaue Ergebnisse liefern kann.</p>
     * <p>Siehe:</p>
     * https://floating-point-gui.de/languages/java/
     *
     * @param color             Der Farbcode
     * @param brightnessPercent Die Helligkeit in Prozent (0.0-1.0)
     * @return "Gedimmter" Farbcode
     */
    private int adjustBrightness(int color, double brightnessPercent) {
        double d = (color / 255.0d * (brightnessPercent * 255.0d));
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * Bereitet das Bild auf, damit es auf ein Panel passt. Ist das Bild zu klein, wird es auf die geforderte Grösse
     * hochskaliert. Ist es zu gross, so wird nur der entsprechende Teilbereich abgebildet, der auf das Panel passt.
     * <p>
     * Gemessen wird an der oberen linken Ecke.
     *
     * @param image          das eingelesene Bild
     * @param ledPanelWidth  Die angegebene Panelbreite
     * @param ledPanelHeight Die angegebene Panelhöhe
     * @return aufbereitetes Bild.
     */
//    private BufferedImage automaticScaleImageToGivenPanelSize(BufferedImage image, int ledPanelWidth, int ledPanelHeight) {
//        ArrayList<Integer> scaleParameters = new ArrayList<>();
//        scaleParameters.add(0); // Start x
//        scaleParameters.add(0); // Start y
//        scaleParameters.add(ledPanelWidth); // width (Länge)
//        scaleParameters.add(ledPanelHeight); // height (Höhe)
//
//        // Vergrössert das Bild auf die Panelgrösse.
//        if (image.getWidth() < ledPanelWidth || image.getHeight() < ledPanelHeight) {
//            image = imageTransformation.getScaledImage(image, new Dimension(ledPanelWidth, ledPanelHeight));
//        }
//        // Anschliessend wird das Bild auf die Panelgrösse geschnitten.
//        return imageTransformation.getSubimage(image, scaleParameters);
//    }

    /**
     * Falls ein Bild für das Panel zu klein ist (Standard = 16x16), soll es auf das Mass vergrössert werden ohne, dass
     * es verzogen oder skaliert wird. Dies wird erreicht in dem einfach ein 16x16 Pixel Bild verwendet wird und darauf
     * das zu kleine Bild kopiert wird. Der "fehlende" bereich vom original Bild wird einfach schwarz und somit auf dem
     * Board nicht angezeigt.
     *
     * @param image das eingelesene Bild
     * @return das auf 16x16 Pixel vergrösserte Bild.
     */
    private BufferedImage handleTooSmallImages(BufferedImage image) {
        final int PANEL_SIZE = 16;
        // Buffered Image in Color-Array laden
        Color[][] colors = getRgbColor2dArray(image);
        BufferedImage newBufferedImage = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_RGB);

        // Setzt das übergebene Bild auf das leere Bild (newBufferedImage). Es wird über das BufferedImage iteriert
        // und falls das übergebene Bild schon vorher endet wird ein schwarzer Pixel hinzugefügt bzw. in diesem Fall
        // wird nichts gemacht, da das leere BufferedImage #000 ist -> also komplett schwarz!
        for (int i = 0; i < PANEL_SIZE; i++) { // i = y
            for (int j = 0; j < PANEL_SIZE; j++) { // j = x
                // Wenn das Bild kleiner als die 16x16 Pixel ist, welche auf die GRB-Matrix übertragen wird, soll einfach
                // ein schwarzer Pixel angezeigt werden.
                if (image.getWidth() > j && image.getHeight() > i) {
                    // Hier wird der Farbcode auf as neue BufferdImage gesetzt -> Gleiche Position falls vorhanden.
                    newBufferedImage.setRGB(j, i, colors[j][i].getRGB()); // j = x, i = y
                }
            }
        }
        return newBufferedImage;
    }

    /**
     * Durchläuft ein Bild Pixel für Pixel und setzt den Farbcode in ein 2D Array ein. Die Position des Farbcodes
     * ist mit dem auf dem Bild identisch.
     *
     * @param image das eingelesene Bild
     * @return Ein Farbarray des Bildes
     */
    private Color[][] getRgbColor2dArray(BufferedImage image) {
        Color[][] colors = new Color[image.getWidth()][image.getHeight()];
        // Geht pixel für Pixel durch und speichert die Farbe im Colors-Array.
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                colors[x][y] = new Color(image.getRGB(x, y));
            }
        }
        return colors;
    }
}