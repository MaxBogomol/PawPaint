package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class RGBA32ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 16;
    }

    @Override
    public int getBitDepth() {
        return 32;
    }

    @Override
    public long getChannelValue() {
        return 4294967295L;
    }

    @Override
    public void encode(Color in, byte[] pixelData) {
        double channelValue = getChannelValue();
        ByteUtil.writeUInt32(pixelData, 0, Math.round(in.r * channelValue));
        ByteUtil.writeUInt32(pixelData, 4, Math.round(in.g * channelValue));
        ByteUtil.writeUInt32(pixelData, 8, Math.round(in.b * channelValue));
        ByteUtil.writeUInt32(pixelData, 12, Math.round(in.a * channelValue));
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        double invertedChannelValue = 1.0f / getChannelValue();
        out.r = (float) (ByteUtil.readUInt32(pixelData, 0) * invertedChannelValue);
        out.g = (float) (ByteUtil.readUInt32(pixelData, 4) * invertedChannelValue);
        out.b = (float) (ByteUtil.readUInt32(pixelData, 8) * invertedChannelValue);
        out.a = (float) (ByteUtil.readUInt32(pixelData, 12) * invertedChannelValue);
    }
}
