package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public class VividLightBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace) {
        float asFactor = (1.0f - ad) * as;
        float adFactor = (1.0f - as) * ad;
        float blend;
        if (s < 0.5f) {
            blend = s <= 0.0f ? 0.0f : Math.max(0.0f, 1.0f - ((1.0f - d) / (2.0f * s)));
        } else {
            blend = s >= 1.0f ? 1.0f : Math.min(1.0f, d / (2.0f * (1.0f - s)));
        }
        return s * asFactor + d * adFactor + as * ad * blend * blendFactor;
    }
}