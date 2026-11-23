package dev.maxbogomol.paw_paint.core.color.blend;

public class NormalBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor) {
        float adFactor = (1.0f - as) * ad;
        float o = s * as + d * adFactor * blendFactor;
        return Math.min(1.0f, o);
    }
}
