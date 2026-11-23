package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;

public abstract class ColorSpace {
    public abstract void encode(Color in, Color out);
    public abstract void decode(Color in, Color out);

    public abstract float min(float value);
    public abstract float max(float value);
    public abstract float clamp(float value);

    public void encode(Color color) {
        encode(color, color);
    }

    public void decode(Color color) {
        decode(color, color);
    }

    public static void clampOut(Color color) {
        color.r = Math.clamp(color.r, 0.0f, 1.0f);
        color.g = Math.clamp(color.g, 0.0f, 1.0f);
        color.b = Math.clamp(color.b, 0.0f, 1.0f);
        color.a = Math.clamp(color.a, 0.0f, 1.0f);
    }
}
