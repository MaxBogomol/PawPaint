package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class LinearBlendMode extends BlendMode {
    @Override
    public void blend(float[] src, float[] dest, float[] out) {
        float as = src[3];
        float ad = dest[3];
        float ao = as + (1.0f - as) * ad;

        out[3] = ao;

        if (ao == 0) {
            out[0] = 0;
            out[1] = 0;
            out[2] = 0;
        } else {
            float aoInverted = 1.0f / ao;
            float adFactor = (1.0f - as) * ad;
            for (int i = 0; i < 3; i++) {
                float sLinear = ColorUtil.srgbToLinear(src[i]);
                float dLinear = ColorUtil.srgbToLinear(dest[i]);
                float oLinear = (sLinear * as + dLinear * adFactor) * aoInverted;
                out[i] = ColorUtil.linearToSrgb(oLinear);
            }
        }
    }
}
