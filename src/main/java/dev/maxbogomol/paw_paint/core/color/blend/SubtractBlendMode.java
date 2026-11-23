package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public class SubtractBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace) {
        float asFactor = (1.0f - ad) * as;
        float adFactor = (1.0f - as) * ad;
        float blend = colorSpace.max(d - s);
        return s * asFactor + d * adFactor + as * ad * blend * blendFactor;
    }
}
