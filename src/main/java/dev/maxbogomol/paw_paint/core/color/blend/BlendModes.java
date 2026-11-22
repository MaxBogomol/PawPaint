package dev.maxbogomol.paw_paint.core.color.blend;

public class BlendModes {
    public static BlendMode NORMAL_LEGACY = new NormalLegacyBlendMode();
    public static BlendMode ADDITIVE_LEGACY = new AdditiveLegacyBlendMode();

    public static BlendMode NORMAL = new NormalBlendMode();
    public static BlendMode ADDITIVE = new AdditiveBlendMode();
}
