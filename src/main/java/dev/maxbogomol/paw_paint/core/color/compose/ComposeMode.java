package dev.maxbogomol.paw_paint.core.color.compose;

import dev.maxbogomol.paw_paint.core.color.Color;

public abstract class ComposeMode {
    public abstract float blend(float src, float dest);

    public void apply(Color src, Color dest) {}
}
