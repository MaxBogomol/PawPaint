package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class LinearRGBColorSpace extends ColorSpace {
    @Override
    public void encode(Color in, Color out) {
        out.r = ColorUtil.srgbToLinearRgb(in.r);
        out.g = ColorUtil.srgbToLinearRgb(in.g);
        out.b = ColorUtil.srgbToLinearRgb(in.b);
    }

    @Override
    public void decode(Color in, Color out) {
        out.r = ColorUtil.linearRgbToSrgb(in.r);
        out.g = ColorUtil.linearRgbToSrgb(in.g);
        out.b = ColorUtil.linearRgbToSrgb(in.b);
    }
}
