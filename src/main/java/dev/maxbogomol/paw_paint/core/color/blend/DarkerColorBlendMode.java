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
            float asFactor = (1.0f - dest.a) * src.a;
            float adFactor = (1.0f - src.a) * dest.a;

            colorSpace.encode(src, srcSpace);
            colorSpace.encode(dest, destSpace);
            composeMode.apply(srcSpace, destSpace);

            float sLuminescence = ColorUtil.rgbToLuminescence(srcSpace.r, srcSpace.g, srcSpace.b);
            float dLuminescence = ColorUtil.rgbToLuminescence(destSpace.r, destSpace.g, destSpace.b);

            boolean darker = sLuminescence < dLuminescence;
            float r = darker ? srcSpace.r : destSpace.r;
            float g = darker ? srcSpace.g : destSpace.g;
            float b = darker ? srcSpace.b : destSpace.b;

            out.r = srcSpace.r * asFactor + destSpace.r * adFactor + src.a * dest.a * r * blendFactor;
            out.g = srcSpace.g * asFactor + destSpace.g * adFactor + src.a * dest.a * g * blendFactor;
            out.b = srcSpace.b * asFactor + destSpace.b * adFactor + src.a * dest.a * b * blendFactor;
            colorSpace.decode(out);
        }
    }
}