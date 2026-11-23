package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class DarkerColorBlendMode extends BlendMode {
    @Override
    public void blend(Color src, Color dest, Color out, ColorSpace colorSpace) {
        float ao = src.a + (1.0f - src.a) * dest.a;
        out.a = ao;

        if (ao == 0) {
            out.r = out.g = out.b = 0;
        } else {
            float blendFactor = 1.0f / ao;
            float asFactor = (1.0f - dest.a) * src.a;
            float adFactor = (1.0f - src.a) * dest.a;

            float rs = colorSpace.encodeRed(src.r);
            float gs = colorSpace.encodeGreen(src.g);
            float bs = colorSpace.encodeBlue(src.b);

            float rd = colorSpace.encodeRed(dest.r);
            float gd = colorSpace.encodeGreen(dest.g);
            float bd = colorSpace.encodeBlue(dest.b);

            float sLuminescence = ColorUtil.rgbToLuminescence(rs, gs, bs);
            float dLuminescence = ColorUtil.rgbToLuminescence(rd, gd, bd);

            boolean darker = sLuminescence < dLuminescence;
            float r = darker ? rs : rd;
            float g = darker ? gs : gd;
            float b = darker ? bs : bd;

            out.r = colorSpace.decodeRed(rs * asFactor + rd * adFactor + src.a * dest.a * r * blendFactor);
            out.g = colorSpace.decodeGreen(gs * asFactor + gd * adFactor + src.a * dest.a * g * blendFactor);
            out.b = colorSpace.decodeBlue(bs * asFactor + bd * adFactor + src.a * dest.a * b * blendFactor);
        }
    }
}