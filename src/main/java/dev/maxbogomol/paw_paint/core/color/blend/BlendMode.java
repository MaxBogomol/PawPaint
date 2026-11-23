package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public abstract class BlendMode {
    public abstract void blend(Color src, Color dest, Color out, ColorSpace colorSpace);
}
