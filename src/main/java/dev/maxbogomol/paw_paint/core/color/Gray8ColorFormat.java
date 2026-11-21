package dev.maxbogomol.paw_paint.core.color;

public class Gray8ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 2;
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
        pixelData[1] = (byte) Math.round(a * channelValue);
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        float gray = (pixelData[0] & 0xFF) * invertedChannelValue;
        out[0] = gray;
        out[1] = gray;
        out[2] = gray;
        out[3] = (pixelData[1] & 0xFF) * invertedChannelValue;
    }
}
