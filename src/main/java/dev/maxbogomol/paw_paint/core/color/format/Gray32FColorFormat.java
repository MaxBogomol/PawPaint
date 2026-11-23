package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;
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
    public void encode(Color in, byte[] pixelData) {
        ByteUtil.writeFloat(pixelData, 0, in.r);
        ByteUtil.writeFloat(pixelData, 4, in.a);
    }

    @Override
    public void decode(Color out, byte[] pixelData) {
        float gray = ByteUtil.readFloat(pixelData, 0);
        out.r = out.g = out.b = gray;
        out.a = ByteUtil.readFloat(pixelData, 4);
    }
}
