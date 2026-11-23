package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class DarkerColorBlendMode extends BlendMode {
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

            float asFactor = (1.0f - destSpace.a) * srcSpace.a;
            float adFactor = (1.0f - srcSpace.a) * destSpace.a;

            float sLuminescence = ColorUtil.rgbToLuminescence(srcSpace.r, srcSpace.g, srcSpace.b);
            float dLuminescence = ColorUtil.rgbToLuminescence(destSpace.r, destSpace.g, destSpace.b);

            boolean darker = sLuminescence < dLuminescence;
            float r = darker ? srcSpace.r : destSpace.r;
            float g = darker ? srcSpace.g : destSpace.g;
            float b = darker ? srcSpace.b : destSpace.b;

            out.r = srcSpace.r * asFactor + destSpace.r * adFactor + srcSpace.a * destSpace.a * r * blendFactor;
            out.g = srcSpace.g * asFactor + destSpace.g * adFactor + srcSpace.a * destSpace.a * g * blendFactor;
            out.b = srcSpace.b * asFactor + destSpace.b * adFactor + srcSpace.a * destSpace.a * b * blendFactor;
            colorSpace.decode(out);
        }
    }
}