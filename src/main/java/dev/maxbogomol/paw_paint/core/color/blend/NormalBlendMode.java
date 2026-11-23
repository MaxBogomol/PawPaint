package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public class NormalBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor, ColorSpace colorSpace) {
        float adFactor = (1.0f - as) * ad;
        return s * as + d * adFactor * blendFactor;
    }
}
