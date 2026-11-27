package dev.maxbogomol.paw_paint.core.color.compose;

import dev.maxbogomol.paw_paint.core.color.Color;

public class SrcOutComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return src * (1.0f - dest);
    }

    @Override
    public void apply(Color src, Color dest) {
        dest.r = dest.g = dest.b = dest.a = 0;
    }
}
