package dev.maxbogomol.paw_paint.core.color;

import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class Gray32ColorFormat extends ColorFormat {
    @Override
    public int getBytesPerPixel() {
        return 8;
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
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        double channelValue = getChannelValue();
        ByteUtil.writeUInt32(pixelData, 0, Math.round(r * channelValue));
        ByteUtil.writeUInt32(pixelData, 4, Math.round(a * channelValue));
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        double invertedChannelValue = 1.0f / getChannelValue();
        float gray = (float) (ByteUtil.readUInt32(pixelData, 0) * invertedChannelValue);
        out[0] = gray;
        out[1] = gray;
        out[2] = gray;
        out[3] = (float) (ByteUtil.readUInt32(pixelData, 4) * invertedChannelValue);
    }
}
