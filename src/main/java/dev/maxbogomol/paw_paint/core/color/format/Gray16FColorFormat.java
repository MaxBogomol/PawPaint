package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;
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
    public void encode(Color in, byte[] pixelData) {
        ByteUtil.writeUInt16(pixelData, 0, Float.floatToFloat16(in.r));
        ByteUtil.writeUInt16(pixelData, 2, Float.floatToFloat16(in.a));
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float gray = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 0));
        out.r = out.g = out.b = gray;
        out.a = Float.float16ToFloat((short) ByteUtil.readUInt16(pixelData, 2));
    }
}
