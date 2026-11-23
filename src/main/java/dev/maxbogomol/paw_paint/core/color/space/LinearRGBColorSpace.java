package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class LinearRGBColorSpace extends ColorSpace {
    @Override
    public void encode(Color in, Color out) {
        out.r = ColorUtil.srgbToLinearRgb(in.r);
        out.g = ColorUtil.srgbToLinearRgb(in.g);
        out.b = ColorUtil.srgbToLinearRgb(in.b);
        out.a = in.a;
    }

    @Override
    public void decode(Color in, Color out) {
        out.r = ColorUtil.linearRgbToSrgb(in.r);
        out.g = ColorUtil.linearRgbToSrgb(in.g);
        out.b = ColorUtil.linearRgbToSrgb(in.b);
        out.a = in.a;
        clampOut(out);
    }

    @Override
    public float min(float value) {
        return Math.min(1.0f, value);
    }

    @Override
    public float max(float value) {
        return Math.max(0.0f, value);
    }

    @Override
    public float clamp(float value) {
        return Math.clamp(value, 0.0f, 1.0f);
    }
}
