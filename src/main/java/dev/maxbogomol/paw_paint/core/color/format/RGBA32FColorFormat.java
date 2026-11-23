package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;
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
    public void encode(Color in, byte[] pixelData) {
        ByteUtil.writeFloat(pixelData, 0, in.r);
        ByteUtil.writeFloat(pixelData, 4, in.g);
        ByteUtil.writeFloat(pixelData, 8, in.b);
        ByteUtil.writeFloat(pixelData, 12, in.a);
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        out.r = ByteUtil.readFloat(pixelData, 0);
        out.g = ByteUtil.readFloat(pixelData, 4);
        out.b = ByteUtil.readFloat(pixelData, 8);
        out.a = ByteUtil.readFloat(pixelData, 12);
    }
}
