package dev.maxbogomol.paw_paint.core.color.blend;

public class ScreenBlendMode extends SimpleBlendMode {
    @Override
    public float blendChannel(float s, float d, float as, float ad, float blendFactor) {
        float asFactor = (1.0f - ad) * as;
        float adFactor = (1.0f - as) * ad;
        float blend = 1.0f - (1.0f - s) * (1.0f - d);
        float o = s * asFactor + d * adFactor + as * ad * blend * blendFactor;
        return Math.min(1.0f, o);
    }
}