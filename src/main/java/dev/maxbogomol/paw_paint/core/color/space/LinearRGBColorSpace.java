package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class LinearRGBColorSpace extends ColorSpace {
    @Override
    public void encode(Color in, Color out) {
        out.r = ColorUtil.srgbToLinear(in.r);
        out.g = ColorUtil.srgbToLinear(in.g);
        out.b = ColorUtil.srgbToLinear(in.b);
        out.a = ColorUtil.srgbToLinear(in.a);
    }

    @Override
    public void decode(Color in, Color out) {
        out.r = ColorUtil.linearToSrgb(in.r);
        out.g = ColorUtil.linearToSrgb(in.g);
        out.b = ColorUtil.linearToSrgb(in.b);
        out.a = ColorUtil.linearToSrgb(in.a);
    }

    @Override
    public float encodeRed(float value) {
        return ColorUtil.srgbToLinear(value);
    }

    @Override
    public float encodeGreen(float value) {
        return ColorUtil.srgbToLinear(value);
    }

    @Override
    public float encodeBlue(float value) {
        return ColorUtil.srgbToLinear(value);
    }

    @Override
    public float encodeAlpha(float value) {
        return value;
    }

    @Override
    public float decodeRed(float value) {
        return ColorUtil.linearToSrgb(value);
    }

    @Override
    public float decodeGreen(float value) {
        return ColorUtil.linearToSrgb(value);
    }

    @Override
    public float decodeBlue(float value) {
        return ColorUtil.linearToSrgb(value);
    }

    @Override
    public float decodeAlpha(float value) {
        return value;
    }
}
