package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public abstract class SimpleBlendMode extends BlendMode {
    @Override
    public void blend(Color src, Color dest, Color out, ColorSpace colorSpace) {
        float ao = src.a + (1.0f - src.a) * dest.a;
        out.a = ao;

        if (ao == 0) {
            out.r = out.g = out.b = 0;
        } else {
            float blendFactor = 1.0f / ao;
            out.r = colorSpace.decodeRed(blendChannel(colorSpace.encodeRed(src.r), colorSpace.encodeRed(dest.r), src.a, dest.a, blendFactor));
            out.g = colorSpace.decodeGreen(blendChannel(colorSpace.encodeGreen(src.g), colorSpace.encodeGreen(dest.g), src.a, dest.a, blendFactor));
            out.b = colorSpace.decodeBlue(blendChannel(colorSpace.encodeBlue(src.b), colorSpace.encodeBlue(dest.b), src.a, dest.a, blendFactor));
        }
    }

    public abstract float blendChannel(float s, float d, float as, float ad, float blendFactor);
}
