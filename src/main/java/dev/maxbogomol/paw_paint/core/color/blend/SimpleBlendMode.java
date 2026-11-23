package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public abstract class SimpleBlendMode extends BlendMode {
    @Override
    public void blend(Color src, Color dest, Color srcSpace, Color destSpace, Color out, ComposeMode composeMode, ColorSpace colorSpace) {
        float ao = composeMode.blend(src.a, dest.a);
        out.a = ao;

        if (ao == 0) {
            out.r = out.g = out.b = 0;
        } else {
            float blendFactor = 1.0f / ao;
            colorSpace.encode(src, srcSpace);
            colorSpace.encode(dest, destSpace);
            composeMode.apply(srcSpace, destSpace);
            out.r = blendChannel(srcSpace.r, destSpace.r, srcSpace.a, destSpace.a, blendFactor, colorSpace);
            out.g = blendChannel(srcSpace.g, destSpace.g, srcSpace.a, destSpace.a, blendFactor, colorSpace);
            out.b = blendChannel(srcSpace.b, destSpace.b, srcSpace.a, destSpace.a, blendFactor, colorSpace);
            colorSpace.decode(out);
        }
    }

    public abstract float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace);
}
