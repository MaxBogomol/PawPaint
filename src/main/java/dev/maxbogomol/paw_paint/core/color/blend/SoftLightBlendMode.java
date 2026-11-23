package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public class SoftLightBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace) {
        float asFactor = (1.0f - ad) * as;
        float adFactor = (1.0f - as) * ad;
        float blend = s < 0.5f ? (float) ((2.0f * s - 1.0f) * (d - Math.pow(d, 2)) + d) : (float) ((2.0f * s - 1.0f) * (Math.sqrt(d) - d) + d);
        return s * asFactor + d * adFactor + as * ad * blend * blendFactor;
    }
}