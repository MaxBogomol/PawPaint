package dev.maxbogomol.paw_paint.core.color;

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
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        float channelValue = getChannelValue();
        ByteUtil.writeUInt16(pixelData, 0, Math.round(r * channelValue));
        ByteUtil.writeUInt16(pixelData, 2, Math.round(a * channelValue));
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float invertedChannelValue = 1.0f / getChannelValue();
        float gray = ByteUtil.readUInt16(pixelData, 0) * invertedChannelValue;
        out[0] = gray;
        out[1] = gray;
        out[2] = gray;
        out[3] = ByteUtil.readUInt16(pixelData, 2) * invertedChannelValue;
    }
}
