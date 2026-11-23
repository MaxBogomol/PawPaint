package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public class OverlayBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace) {
        float asFactor = (1.0f - ad) * as;
        float adFactor = (1.0f - as) * ad;
        float blend = d < 0.5f ? 2.0f * s * d : 1.0f - 2.0f * (1.0f - s) * (1.0f - d);
        return s * asFactor + d * adFactor + as * ad * blend * blendFactor;
    }
}