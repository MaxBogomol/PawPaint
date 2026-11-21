package dev.maxbogomol.paw_paint.core.color;

import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class Gray16FColorFormat extends ColorFormat {
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
        return 1;
    }

    @Override
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        ByteUtil.writeUInt16(pixelData, 0, Float.floatToFloat16(r));
        ByteUtil.writeUInt16(pixelData, 2, Float.floatToFloat16(a));
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        float gray = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 0));
        out[0] = gray;
        out[1] = gray;
        out[2] = gray;
        out[3] = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 2));
    }
}
