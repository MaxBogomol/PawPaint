package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public abstract class SimpleBlendMode extends BlendMode {
    @Override
    public void blend(Color src, Color dest, Color srcSpace, Color destSpace, Color out, ColorSpace colorSpace) {
        float ao = src.a + (1.0f - src.a) * dest.a;
        out.a = ao;

        if (ao == 0) {
            out.r = out.g = out.b = 0;
        } else {
            float blendFactor = 1.0f / ao;
            colorSpace.encode(src, srcSpace);
            colorSpace.encode(dest, destSpace);
            out.r = blendChannel(srcSpace.r, destSpace.r, src.a, dest.a, blendFactor);
            out.g = blendChannel(srcSpace.g, destSpace.g, src.a, dest.a, blendFactor);
            out.b = blendChannel(srcSpace.b, destSpace.b, src.a, dest.a, blendFactor);
            colorSpace.decode(out);
        }
    }

    public abstract float blendChannel(float s, float d, float as, float ad, float blendFactor);
}
