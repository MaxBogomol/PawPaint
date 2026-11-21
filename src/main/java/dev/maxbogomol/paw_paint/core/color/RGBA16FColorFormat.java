package dev.maxbogomol.paw_paint.core.color;

import dev.maxbogomol.paw_paint.core.util.ByteUtil;

public class RGBA16FColorFormat extends ColorFormat {
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
        return 1;
    }

    @Override
    public void encode(float r, float g, float b, float a, byte[] pixelData) {
        ByteUtil.writeUInt16(pixelData, 0, Float.floatToFloat16(r));
        ByteUtil.writeUInt16(pixelData, 2, Float.floatToFloat16(g));
        ByteUtil.writeUInt16(pixelData, 4, Float.floatToFloat16(b));
        ByteUtil.writeUInt16(pixelData, 6, Float.floatToFloat16(a));
    }

    @Override
    public void decode(float[] out, byte[] pixelData) {
        out[0] = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 0));
        out[1] = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 2));
        out[2] = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 4));
        out[3] = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 6));
    }
}
