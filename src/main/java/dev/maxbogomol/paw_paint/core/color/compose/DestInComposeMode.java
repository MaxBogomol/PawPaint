package dev.maxbogomol.paw_paint.core.color.compose;

import dev.maxbogomol.paw_paint.core.color.Color;

public class DestInComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return dest * src;
    }

    @Override
    public void apply(Color src, Color dest) {
        Color.swap(src, dest);
        dest.r = dest.g = dest.b = dest.a = 0;
    }
}
