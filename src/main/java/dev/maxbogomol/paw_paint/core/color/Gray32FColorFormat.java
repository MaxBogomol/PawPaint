package dev.maxbogomol.paw_paint.core.color;

import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class Gray32FColorFormat extends ColorFormat {
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
        return 1;
    }

    @Override
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        ByteUtil.writeFloat(pixelData, 0, r);
        ByteUtil.writeFloat(pixelData, 4, a);
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float gray = ByteUtil.readFloat(pixelData, 0);
        out[0] = gray;
        out[1] = gray;
        out[2] = gray;
        out[3] = ByteUtil.readFloat(pixelData, 4);
    }
}
