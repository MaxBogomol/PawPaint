package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;

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
    public void encode(Color in, byte[] pixelData) {
        float channelValue = getChannelValue();
        int gray5 = Math.round(in.r * channelValue);
        int a5 = Math.round(in.a * channelValue);
        int packed = (gray5 << 11) | (a5 << 6);
        pixelData[0] = (byte) ((packed >> 8) & 0xFF);
        pixelData[1] = (byte) (packed & 0xFF);
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        int packed = ((pixelData[0] & 0xFF) << 8) | (pixelData[1] & 0xFF);
        float gray = ((packed >> 11) & 0x1F) * invertedChannelValue;
        out.r = out.g = out.b = gray;
        out.a = ((packed >> 6) & 0x1F) * invertedChannelValue;
    }
}
