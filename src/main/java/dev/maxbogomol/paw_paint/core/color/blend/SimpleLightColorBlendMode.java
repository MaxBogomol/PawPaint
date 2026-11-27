package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public abstract class SimpleLightColorBlendMode extends BlendMode {
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

            float asFactor = (1.0f - c.destSpace.a) * c.srcSpace.a;
            float adFactor = (1.0f - c.srcSpace.a) * c.destSpace.a;

            float sLuminescence = ColorUtil.rgbToLuminescence(c.srcSpace.r, c.srcSpace.g, c.srcSpace.b);
            float dLuminescence = ColorUtil.rgbToLuminescence(c.destSpace.r, c.destSpace.g, c.destSpace.b);

            boolean darker = isDarker(sLuminescence, dLuminescence);
            float r = darker ? c.srcSpace.r : c.destSpace.r;
            float g = darker ? c.srcSpace.g : c.destSpace.g;
            float b = darker ? c.srcSpace.b : c.destSpace.b;

            c.out.r = c.srcSpace.r * asFactor + c.destSpace.r * adFactor + c.srcSpace.a * c.destSpace.a * r * blendFactor;
            c.out.g = c.srcSpace.g * asFactor + c.destSpace.g * adFactor + c.srcSpace.a * c.destSpace.a * g * blendFactor;
            c.out.b = c.srcSpace.b * asFactor + c.destSpace.b * adFactor + c.srcSpace.a * c.destSpace.a * b * blendFactor;
            colorSpace.decode(c.out);
        }
    }

    public abstract boolean isDarker(float sLuminescence, float dLuminescence);
}