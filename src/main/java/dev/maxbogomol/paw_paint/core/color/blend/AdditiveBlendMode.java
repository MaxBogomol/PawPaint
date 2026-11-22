package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class AdditiveBlendMode extends BlendMode {
    @Override
    public void blend(float[] src, float[] dest, float[] out) {
        float ao = src[3] + (1.0f - src[3]) * dest[3];
        out[3] = ao;

        if (ao == 0) {
            out[0] = out[1] = out[2] = 0;
        } else {
            float blendFactor = 1.0f / ao;
            for (int i = 0; i < 3; i++) {
                float sLinear = ColorUtil.srgbToLinear(src[i]);
                float dLinear = ColorUtil.srgbToLinear(dest[i]);
                float oLinear = (sLinear * src[3] + dLinear * dest[3]) * blendFactor;
                out[i] = ColorUtil.linearToSrgb(Math.min(1.0f, oLinear));
            }
        }
    }
}
