package dev.maxbogomol.paw_paint.core.color;

public class IndexColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 1;
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
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        float index = (pixelData[0] & 0xFF) * invertedChannelValue;
        out[0] = index;
        out[1] = index;
        out[2] = index;
        out[3] = index;
    }
}
