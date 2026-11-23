package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public abstract class SimpleBlendMode extends BlendMode {
    @Override
    public void blend(Color src, Color dest, Color out) {
        float ao = src.a + (1.0f - src.a) * dest.a;
        out.a = ao;

        if (ao == 0) {
            out.r = out.g = out.b = 0;
        } else {
            float blendFactor = 1.0f / ao;
            out.r = ColorUtil.linearToSrgb(blendChannel(ColorUtil.srgbToLinear(src.r), ColorUtil.srgbToLinear(dest.r), src.a, dest.a, blendFactor));
            out.g = ColorUtil.linearToSrgb(blendChannel(ColorUtil.srgbToLinear(src.g), ColorUtil.srgbToLinear(dest.g), src.a, dest.a, blendFactor));
            out.b = ColorUtil.linearToSrgb(blendChannel(ColorUtil.srgbToLinear(src.b), ColorUtil.srgbToLinear(dest.b), src.a, dest.a, blendFactor));
        }
    }

    public abstract float blendChannel(float s, float d, float as, float ad, float blendFactor);
}
