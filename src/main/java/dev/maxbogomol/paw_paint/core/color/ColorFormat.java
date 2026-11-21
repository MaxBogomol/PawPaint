package dev.maxbogomol.paw_paint.core.color;

public abstract class ColorFormat {
    private byte[] pixelData;

    public void createPixelData() {
        if (pixelData == null) pixelData = new byte[getBytesPerPixel()];
    }

    public byte[] getPixelData() {
        return pixelData;
    }

    public abstract int getBytesPerPixel();
    public abstract int getBitDepth();
    public abstract long getChannelValue();

    public abstract void encode(float r, float g, float b, float a, byte[] pixelData);
    public abstract void decode(float[] out , byte[] pixelData);

    public static float[] createColor() {
        return new float[4];
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
