package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;

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
    public void encode(Color in, byte[] pixelData) {
        float channelValue = getChannelValue();
        pixelData[0] = (byte) Math.round(in.r * channelValue);
        pixelData[1] = (byte) Math.round(in.g * channelValue);
        pixelData[2] = (byte) Math.round(in.b * channelValue);
        pixelData[3] = (byte) Math.round(in.a * channelValue);
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        out.r = (pixelData[0] & 0xFF) * invertedChannelValue;
        out.g = (pixelData[1] & 0xFF) * invertedChannelValue;
        out.b = (pixelData[2] & 0xFF) * invertedChannelValue;
        out.a = (pixelData[3] & 0xFF) * invertedChannelValue;
    }
}
