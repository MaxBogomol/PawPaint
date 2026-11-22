package dev.maxbogomol.paw_paint.core.color;

import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public abstract class ColorFormat {
    public abstract int getBytesPerPixel();
    public abstract int getBitDepth();
    public abstract long getChannelValue();

    public abstract void encode(float r, float g, float b, float a, byte[] pixelData);
    public abstract void decode(float[] out, byte[] pixelData);

    public void encode(float[] in, byte[] pixelData) {
        encode(in[0], in[1], in[2], in[3], pixelData);
    }

    public byte[] createPixelData() {
        return new byte[getBytesPerPixel()];
    }

    public static float[] createColor() {
        return new float[4];
    }

    public static float[] createColor(float r, float g, float b, float a) {
        float[] color = createColor();
        ColorUtil.setColor(r, g, b, a, color);
        return color;
    }

    public static float getRed(float[] color) {
        return color[0];
    }

    public static float getGreen(float[] color) {
        return color[1];
    }

    public static float getBlue(float[] color) {
        return color[2];
    }

    public static float getAlpha(float[] color) {
        return color[3];
    }
}
