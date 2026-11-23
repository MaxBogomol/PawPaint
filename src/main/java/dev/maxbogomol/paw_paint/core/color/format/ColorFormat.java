package dev.maxbogomol.paw_paint.core.color.format;

import dev.maxbogomol.paw_paint.core.color.Color;

public abstract class ColorFormat {
    public abstract int getBytesPerPixel();
    public abstract int getBitDepth();
    public abstract long getChannelValue();

    public abstract void encode(Color in, byte[] pixelData);
    public abstract void decode(Color out, byte[] pixelData);

    public byte[] createPixelData() {
        return new byte[getBytesPerPixel()];
    }
}
