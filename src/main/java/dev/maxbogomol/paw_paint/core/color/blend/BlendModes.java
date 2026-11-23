package dev.maxbogomol.paw_paint.core.color.blend;

public class BlendModes {
    public static BlendMode NORMAL = new NormalBlendMode();
    //public static BlendMode DISSOLVE = new NormalBlendMode();

    public static BlendMode DARKEN_ONLY = new DarkenOnlyBlendMode();
    public static BlendMode DARKER_COLOR = new DarkerColorBlendMode();
    public static BlendMode MULTIPLY = new MultiplyBlendMode();
    public static BlendMode COLOR_BURN = new ColorBurnBlendMode();
    public static BlendMode LINEAR_BURN = new LinearBurnBlendMode();

    public static BlendMode LIGHTEN_ONLY = new LightenOnlyBlendMode();
    public static BlendMode LIGHTER_COLOR = new LighterColorBlendMode();
    public static BlendMode SCREEN = new ScreenBlendMode();
    public static BlendMode COLOR_DODGE = new ColorDodgeBlendMode();
    public static BlendMode LINEAR_DODGE = new LinearDodgeBlendMode();

    public static BlendMode DIVIDE = new DivideBlendMode();
    public static BlendMode ADDITION = new AdditionBlendMode();
    public static BlendMode SUBTRACT = new SubtractBlendMode();
    public static BlendMode DIFFERENCE = new DifferenceBlendMode();

    public static BlendMode OVERLAY = new OverlayBlendMode();
    public static BlendMode SOFT_LIGHT = new SoftLightBlendMode();
    public static BlendMode HARD_LIGHT = new HardLightBlendMode();
    public static BlendMode VIVID_LIGHT = new VividLightBlendMode();
    public static BlendMode LINEAR_LIGHT = new LinearLightBlendMode();
}
