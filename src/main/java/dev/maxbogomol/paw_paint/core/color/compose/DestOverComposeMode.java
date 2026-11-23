package dev.maxbogomol.paw_paint.core.color.compose;

import dev.maxbogomol.paw_paint.core.color.Color;

public class DestOverComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return dest + (1.0f - dest) * src;
    }

    @Override
    public void apply(Color src, Color dest) {
        Color.swap(src, dest);
    }
}
