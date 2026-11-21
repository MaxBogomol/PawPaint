package dev.maxbogomol.paw_paint.core.color;

public class Gray5ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 2;
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
        int gray5 = Math.round(r * channelValue);
        int a5 = Math.round(a * channelValue);
        int packed = (gray5 << 11) | (a5 << 6);
        pixelData[0] = (byte) ((packed >> 8) & 0xFF);
        pixelData[1] = (byte) (packed & 0xFF);
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        int packed = ((pixelData[0] & 0xFF) << 8) | (pixelData[1] & 0xFF);
        float gray = ((packed >> 11) & 0x1F) * invertedChannelValue;
        out[0] = gray;
        out[1] = gray;
        out[2] = gray;
        out[3] = ((packed >> 6) & 0x1F) * invertedChannelValue;
    }
}
