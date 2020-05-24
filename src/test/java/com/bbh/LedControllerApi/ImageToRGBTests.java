package com.bbh.LedControllerApi;

import com.bbh.LedControllerApi.services.imageService.ImageToRGB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ImageToRGBTests {

    @Autowired
    private ImageToRGB imageToRGB;

    @Test
    public void testBuildGrbString_Black() {
        String grbString = imageToRGB.buildGrbString(Color.BLACK);
        assertThat(grbString).isNotNull();
        assertThat(grbString).isEqualTo("000000");
    }

    @Test
    public void testBuildGrbString_WithCustomColor() {
        String grbString = imageToRGB.buildGrbString(new Color(255, 127, 35));
        assertThat(grbString).isNotNull();
        assertThat(grbString).isEqualTo("010300"); // Farbe wird um die Helligkeit 0.01 reduziert
    }

    @Test
    public void testBuildGrbString_WithBrightness() {
        String grbString = imageToRGB.buildGrbString(new Color(255, 127, 35), 1);
        assertThat(grbString).isNotNull();
        assertThat(grbString).isEqualTo("7FFF23"); // Farbe mit 100% Helligkeit
    }

    @Test
    public void testBuildGrbString_WithBrightness_White() {
        String grbString = imageToRGB.buildGrbString(Color.WHITE, 1);
        assertThat(grbString).isNotNull();
        assertThat(grbString).isEqualTo("FFFFFF"); // Farbe mit 100% Helligkeit
    }

    @Test
    public void testBuildGrbString() {
        String[][] imageColorArray = {{"12", "34", "56", "78"}
        , {"9a", "bc", "de", "ff"}
        , {"ff", "de", "cb", "a9"}
        , {"87", "65", "43", "21"}};

        String grbString = imageToRGB.GrbArrayToString(imageColorArray);
        assertThat(grbString).isNotNull();
		assertThat(grbString).isEqualTo("[<0112345678>, <02ffdebc9a>, <03ffdecba9>, <0421436587>]");
    }

    @Test
    public void testBuildGrbStringNonSquareField() {
        String[][] hexColorCode = {{"12", "34", "56"}
                , {"9a", "bc", "de"}
                , {"ff", "de", "cb"}
                , {"87", "65", "43"}};

        // Es wird erwartet, dass ein GRB String quadratisch daher kommt, falls nicht gibt es eine ArrayIndexOutOfBoundsException.
        Exception exception = Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            imageToRGB.GrbArrayToString(hexColorCode);
        });

        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(ArrayIndexOutOfBoundsException.class);
        assertThat(exception.getMessage()).isEqualTo("3");
    }

    @Test
    public void testImageToString() {
        // TODO:
//        imageToRGB.imageToString();
    }
}
