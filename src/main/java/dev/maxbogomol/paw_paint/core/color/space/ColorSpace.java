package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;

public abstract class ColorSpace {
    public abstract void encode(Color in, Color out);
    public abstract void decode(Color in, Color out);

    public void encode(Color color) {
        encode(color, color);
    }

    public void decode(Color color) {
        decode(color, color);
    }
}
