package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;

public class SRGBColorSpace extends ColorSpace {
    @Override
    public void encode(Color in, Color out) {
        in.copy(out);
    }

    @Override
    public void decode(Color in, Color out) {
        in.copy(out);
        clampOut(out);
    }

    @Override
    public float min(float value) {
        return Math.min(1.0f, value);
    }

    @Override
    public float max(float value) {
        return Math.max(0.0f, value);
    }

    @Override
    public float clamp(float value) {
        return Math.clamp(value, 0.0f, 1.0f);
    }
}
