package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;

public class SRGBColorSpace extends ColorSpace {
    @Override
    public void encode(Color in, Color out) {
        in.copy(out);
    }

    @Override
    public void decode(Color in, Color out) {
        in.copy(out);
    }

    @Override
    public float encodeRed(float value) {
        return value;
    }

    @Override
    public float encodeGreen(float value) {
        return value;
    }

    @Override
    public float encodeBlue(float value) {
        return value;
    }

    @Override
    public float encodeAlpha(float value) {
        return value;
    }

    @Override
    public float decodeRed(float value) {
        return value;
    }

    @Override
    public float decodeGreen(float value) {
        return value;
    }

    @Override
    public float decodeBlue(float value) {
        return value;
    }

    @Override
    public float decodeAlpha(float value) {
        return value;
    }
}
