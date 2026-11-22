package dev.maxbogomol.paw_paint.core.color.blend;

public class BlendModes {
    public static BlendMode NORMAL = new NormalBlendMode();

    public static BlendMode DARKEN = new DarkenBlendMode();
    public static BlendMode DARKER_COLOR = new DarkerColorBlendMode();
    public static BlendMode MULTIPLY = new MultiplyBlendMode();

    public static BlendMode LIGHTEN = new LightenBlendMode();
    public static BlendMode LIGHTER_COLOR = new LighterColorBlendMode();
    public static BlendMode SCREEN = new ScreenBlendMode();
    public static BlendMode ADDITIVE = new AdditiveBlendMode();
}
