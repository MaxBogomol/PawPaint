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

    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.601-7-201103-i!!pdf-e.pdf
    public static float RGBtoGrayscaleLegacy(float r, float g, float b) {
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }

    //https://www.itu.int/dms_pubrec/itu-r/rec/bt/r-rec-bt.709-6-201506-i!!pdf-e.pdf
    public static float RGBtoGrayscaleModern(float r, float g, float b) {
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    public static float RGBtoGrayscaleSimple(float r, float g, float b) {
        return (r + g + b) / 3;
    }
}
