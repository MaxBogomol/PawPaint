package dev.maxbogomol.paw_paint.core.color;

import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class RGBA32FColorFormat extends ColorFormat {
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
        return 1;
    }

    @Override
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        ByteUtil.writeFloat(pixelData, 0, r);
        ByteUtil.writeFloat(pixelData, 4, g);
        ByteUtil.writeFloat(pixelData, 8, b);
        ByteUtil.writeFloat(pixelData, 12, a);
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        out[0] = ByteUtil.readFloat(pixelData, 0);
        out[1] = ByteUtil.readFloat(pixelData, 4);
        out[2] = ByteUtil.readFloat(pixelData, 8);
        out[3] = ByteUtil.readFloat(pixelData, 12);
    }
}
