package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;

public abstract class ColorSpace {
    public abstract void encode(Color in, Color out);
    public abstract void decode(Color in, Color out);

    public void encode(Color color) {
        encode(color, color);
    }

    public void decode(Color color) {
        decode(color, color);
    }

    public abstract float encodeRed(float value);
    public abstract float encodeGreen(float value);
    public abstract float encodeBlue(float value);
    public abstract float encodeAlpha(float value);
    public abstract float decodeRed(float value);
    public abstract float decodeGreen(float value);
    public abstract float decodeBlue(float value);
    public abstract float decodeAlpha(float value);
}
