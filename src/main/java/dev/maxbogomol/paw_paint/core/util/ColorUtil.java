package dev.maxbogomol.paw_paint.core.util;

public class ColorUtil {
    public static void setColor(float r, float g, float b, float a, float[] color) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    public static void copyColor(float[] src, float[] dest) {
        System.arraycopy(src, 0, dest, 0, dest.length);
    }

    //ITU-R BT.601-7
    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.601-7-201103-i!!pdf-e.pdf
    public static float rgbToGrayscaleLegacy(float r, float g, float b) {
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }

    //ITU-R BT.709-6
    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.709-6-201506-i!!pdf-e.pdf
    public static float rgbToGrayscaleModern(float r, float g, float b) {
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    public static float rgbToGrayscaleSimple(float r, float g, float b) {
        return (r + g + b) / 3;
    }

    //IEC 61966-2-1
    public static float srgbToLinear(float c) {
        if (c <= 0.04045f) return c / 12.92f;
        return (float) Math.pow((c + 0.055f) / 1.055f, 2.4f);
    }

    //IEC 61966-2-1
    public static float linearToSrgb(float c) {
        if (c <= 0.0031308f) return c * 12.92f;
        return (float) (1.055f * Math.pow(c, 1.0f / 2.4f) - 0.055f);
    }
}
