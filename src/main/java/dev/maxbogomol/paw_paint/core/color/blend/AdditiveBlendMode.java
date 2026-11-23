package dev.maxbogomol.paw_paint.core.color.blend;

public class AdditiveBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor) {
        float o = (s * as + d * ad) * blendFactor;
        return Math.min(1.0f, o);
    }
}
