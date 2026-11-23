package dev.maxbogomol.paw_paint.core.color.blend;

public class MultiplyBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor) {
        float asFactor = (1.0f - ad) * as;
        float adFactor = (1.0f - as) * ad;
        float blend = Math.min(1.0f, s * d);
        float o = s * asFactor + d * adFactor + as * ad * blend * blendFactor;
        return Math.min(1.0f, o);
    }
}