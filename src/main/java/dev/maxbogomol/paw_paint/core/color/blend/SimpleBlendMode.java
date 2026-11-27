package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public abstract class SimpleBlendMode extends BlendMode {
    @Override
    public void blend(BlendContext c, ComposeMode composeMode, ColorSpace colorSpace) {
        float ao = composeMode.blend(c.src.a, c.dest.a);
        c.out.a = ao;

        if (ao == 0) {
            c.out.r = c.out.g = c.out.b = 0;
        } else {
            float blendFactor = 1.0f / ao;
            colorSpace.encode(c.src, c.srcSpace);
            colorSpace.encode(c.dest, c.destSpace);
            composeMode.apply(c.srcSpace, c.destSpace);
            c.out.r = blendChannel(c.srcSpace.r, c.destSpace.r, c.srcSpace.a, c.destSpace.a, blendFactor, colorSpace);
            c.out.g = blendChannel(c.srcSpace.g, c.destSpace.g, c.srcSpace.a, c.destSpace.a, blendFactor, colorSpace);
            c.out.b = blendChannel(c.srcSpace.b, c.destSpace.b, c.srcSpace.a, c.destSpace.a, blendFactor, colorSpace);
            colorSpace.decode(c.out);
        }
    }

    public abstract float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace);
}
