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
    }
}
