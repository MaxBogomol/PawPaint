package dev.maxbogomol.paw_paint.core.color.compose;

public class SrcOutComposeMode extends ComposeMode {
    @Override
    public float blend(float src, float dest) {
        return src + (1.0f - dest);
    }
}
