package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class LighterColorBlendMode extends BlendMode {
    @Override
    public void blend(float[] src, float[] dest, float[] out) {
        float ao = src[3] + (1.0f - src[3]) * dest[3];
        out[3] = ao;

        if (ao == 0) {
            out[0] = out[1] = out[2] = 0;
        } else {
            float blendFactor = 1.0f / ao;
            float asFactor = (1.0f - dest[3]) * src[3];
            float adFactor = (1.0f - src[3]) * dest[3];

            float rs = ColorUtil.srgbToLinear(src[0]);
            float gs = ColorUtil.srgbToLinear(src[1]);
            float bs = ColorUtil.srgbToLinear(src[2]);

            float rd = ColorUtil.srgbToLinear(dest[0]);
            float gd = ColorUtil.srgbToLinear(dest[1]);
            float bd = ColorUtil.srgbToLinear(dest[2]);

            float sLuminescence = ColorUtil.rgbToLuminescence(rs, gs, bs);
            float dLuminescence = ColorUtil.rgbToLuminescence(rd, gd, bd);

            boolean lighter = sLuminescence > dLuminescence;
            float r = lighter ? rs : rd;
            float g = lighter ? gs : gd;
            float b = lighter ? bs : bd;

            out[0] = ColorUtil.linearToSrgb(rs * asFactor + rd * adFactor + src[3] * dest[3] * r * blendFactor);
            out[1] = ColorUtil.linearToSrgb(gs * asFactor + gd * adFactor + src[3] * dest[3] * g * blendFactor);
            out[2] = ColorUtil.linearToSrgb(bs * asFactor + bd * adFactor + src[3] * dest[3] * b * blendFactor);
        }
    }
}