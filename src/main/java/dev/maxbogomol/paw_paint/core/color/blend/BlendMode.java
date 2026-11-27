package dev.maxbogomol.paw_paint.core.color.blend;

import dev.maxbogomol.paw_paint.core.color.compose.ComposeMode;
import dev.maxbogomol.paw_paint.core.color.space.ColorSpace;

public abstract class BlendMode {
    public abstract void blend(BlendContext c, ComposeMode composeMode, ColorSpace colorSpace);
}
