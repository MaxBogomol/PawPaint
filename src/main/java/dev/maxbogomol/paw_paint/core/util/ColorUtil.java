package dev.maxbogomol.paw_paint.core.util;

public class ColorUtil {
    public static float RGBtoGrayscaleLegacy(float r, float g, float b) {
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }

    public static float RGBtoGrayscale(float r, float g, float b) {
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }
}
