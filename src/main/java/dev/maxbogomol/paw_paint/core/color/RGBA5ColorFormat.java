package dev.maxbogomol.paw_paint.core.color;

public class RGBA5ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 3;
    }

    @Override
    public int getBitDepth() {
        return 5;
    }

    @Override
    public long getChannelValue() {
        return 31;
    }

    @Override
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        float channelValue = getChannelValue();
        int r5 = Math.round(r * channelValue);
        int g5 = Math.round(g * channelValue);
        int b5 = Math.round(b * channelValue);
        int a5 = Math.round(a * channelValue);
        int packed = (r5 << 19) | (g5 << 14) | (b5 << 9) | (a5 << 4);
        pixelData[0] = (byte) ((packed >> 16) & 0xFF);
        pixelData[1] = (byte) ((packed >> 8) & 0xFF);
        pixelData[2] = (byte) (packed & 0xFF);
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        int packed = ((pixelData[0] & 0xFF) << 16) | ((pixelData[1] & 0xFF) << 8) | (pixelData[2] & 0xFF);
        out[0] = ((packed >> 19) & 0x1F) * invertedChannelValue;
        out[1] = ((packed >> 14) & 0x1F) * invertedChannelValue;
        out[2] = ((packed >> 9) & 0x1F) * invertedChannelValue;
        out[3] = ((packed >> 4) & 0x1F) * invertedChannelValue;
    }
}
