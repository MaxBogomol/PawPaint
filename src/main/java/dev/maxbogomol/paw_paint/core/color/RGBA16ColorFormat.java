package dev.maxbogomol.paw_paint.core.color;

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
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        float channelValue = getChannelValue();
        ByteUtil.writeUInt16(pixelData, 0, Math.round(r * channelValue));
        ByteUtil.writeUInt16(pixelData, 2, Math.round(g * channelValue));
        ByteUtil.writeUInt16(pixelData, 4, Math.round(b * channelValue));
        ByteUtil.writeUInt16(pixelData, 6, Math.round(a * channelValue));
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        out[0] = ByteUtil.readUInt16(pixelData, 0) * invertedChannelValue;
        out[1] = ByteUtil.readUInt16(pixelData, 2) * invertedChannelValue;
        out[2] = ByteUtil.readUInt16(pixelData, 4) * invertedChannelValue;
        out[3] = ByteUtil.readUInt16(pixelData, 6) * invertedChannelValue;
    }
}
