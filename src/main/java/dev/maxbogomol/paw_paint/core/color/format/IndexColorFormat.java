package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;

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
    public void encode(Color in, byte[] pixelData) {
        float channelValue = getChannelValue();
        pixelData[0] = (byte) Math.round(in.r * channelValue);
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        float index = (pixelData[0] & 0xFF) * invertedChannelValue;
        out.r = out.g = out.b = out.a = index;
    }
}
