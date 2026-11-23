package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class RGBA16ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 8;
    }

    @Override
    public int getBitDepth() {
        return 16;
    }

    @Override
    public long getChannelValue() {
        return 65535;
    }

    @Override
    public void encode(Color in, byte[] pixelData) {
        float channelValue = getChannelValue();
        ByteUtil.writeUInt16(pixelData, 0, Math.round(in.r * channelValue));
        ByteUtil.writeUInt16(pixelData, 2, Math.round(in.g * channelValue));
        ByteUtil.writeUInt16(pixelData, 4, Math.round(in.b * channelValue));
        ByteUtil.writeUInt16(pixelData, 6, Math.round(in.a * channelValue));
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        out.r = ByteUtil.readUInt16(pixelData, 0) * invertedChannelValue;
        out.g = ByteUtil.readUInt16(pixelData, 2) * invertedChannelValue;
        out.b = ByteUtil.readUInt16(pixelData, 4) * invertedChannelValue;
        out.a = ByteUtil.readUInt16(pixelData, 6) * invertedChannelValue;
    }
}
