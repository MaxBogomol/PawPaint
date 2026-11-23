package dev.maxbogomol.paw_paint.core.color.space;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.util.ColorUtil;

public class OKLABColorSpace extends ColorSpace {
    @Override
    public void encode(Color in, Color out) {
        ColorUtil.srgbToOklab(in).copy(out);
    }

    @Override
    public void decode(Color in, Color out) {
        ColorUtil.oklabToSrgb(in).copy(out);
    }
}
