package dev.maxbogomol.paw_paint.core.color;

public class RGBA8ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 4;
    }

    @Override
    public int getBitDepth() {
        return 8;
    }

    @Override
    public long getChannelValue() {
        return 255;
    }

    @Override
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        float channelValue = getChannelValue();
        pixelData[0] = (byte) Math.round(r * channelValue);
        pixelData[1] = (byte) Math.round(g * channelValue);
        pixelData[2] = (byte) Math.round(b * channelValue);
        pixelData[3] = (byte) Math.round(a * channelValue);
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        out[0] = (pixelData[0] & 0xFF) * invertedChannelValue;
        out[1] = (pixelData[1] & 0xFF) * invertedChannelValue;
        out[2] = (pixelData[2] & 0xFF) * invertedChannelValue;
        out[3] = (pixelData[3] & 0xFF) * invertedChannelValue;
    }
}
