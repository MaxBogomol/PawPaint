package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class Gray16ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 4;
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
        ByteUtil.writeUInt16(pixelData, 2, Math.round(in.a * channelValue));
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        float gray = ByteUtil.readUInt16(pixelData, 0) * invertedChannelValue;
        out.r = out.g = out.b = gray;
        out.a = ByteUtil.readUInt16(pixelData, 2) * invertedChannelValue;
    }
}
